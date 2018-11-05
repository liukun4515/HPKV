//
// Created by wangxinshuo on 11/1/18.
//

#include "Utils.h"


Resources::Resources(const char *name,
                     const char *indexFileName,
                     const char *valueFileName) {
    if (mkdir(name, S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH) == -1) {
        if (errno == EEXIST) {
            // already exists
        } else {
            // something else
            std::cout << "Create error:" << strerror(errno) << std::endl;
        }
    }
    std::string dir(name);
    this->indexFile = fopen(dir.append(indexFileName).data(), "a+");
    dir.clear();
    dir.assign(name);
    this->valueFile = fopen(dir.append(valueFileName).data(), "a+");
}

void Resources::writeIndex(const char *data, size_t len) {
    fseek(indexFile, 0L, SEEK_END);
    fwrite(data, sizeof(char), len * (sizeof(char)), indexFile);
    fflush(indexFile);
}

void Resources::readIndex(char *data, size_t start, size_t len) {
    fseek(indexFile, start, SEEK_SET);
    fread(data, sizeof(char), len * (sizeof(char)), indexFile);
}

long long Resources::getIndexFileLength() {
    fseek(indexFile, 0L, SEEK_END);
    return ftell(indexFile);
}

void Resources::writeValue(const char *data, size_t len) {
    fseek(valueFile, 0L, SEEK_END);
    fwrite(data, sizeof(char), len * (sizeof(char)), valueFile);
    fflush(valueFile);
}

void Resources::readValue(char *data, size_t start, size_t len) {
    fseek(valueFile, start, SEEK_SET);
    fread(data, sizeof(char), len * (sizeof(char)), valueFile);
}

long long Resources::getValueFileLength() {
    fseek(valueFile, 0L, SEEK_END);
    return ftell(valueFile);
}

void Resources::close() {
    fclose(indexFile);
    fclose(valueFile);
}

Resources::~Resources() {
    this->close();
}


void Convert::longToCharArray(long long position, char *data) {
    int count = POINTER_LENGTH_IN_INDEX - 1;
    while (count != -1) {
        *(data + count) = (char) (position % 256);
        position = position >> 8;
        count--;
    }
}

long long Convert::charArrayToLong(const char *data) {
    int count = 0;
    unsigned long long result = 0;
    while (count != POINTER_LENGTH_IN_INDEX) {
        result = result << 8;
        result += int(*(data + count));
        count++;
    }
    return result;
}

void Convert::intToCharArray(int position, char *data) {
    int count = VALUE_LENGTH_COUNT - 1;
    while (count != -1) {
        *(data + count) = (char) (position % 256);
        position = position >> 8;
        count--;
    }
}

int Convert::charArrayToInt(const char *data) {
    int count = 0;
    int result = 0;
    while (count != VALUE_LENGTH_COUNT) {
        result = result << 8;
        result += int(*(data + count));
        count++;
    }
    return result;
}


void Rebuild::rebuild(Resources *resources, TreeMap *treeMap) {
    std::cout << "File Length:" << resources->getValueFileLength() << std::endl;
    char charKeySize;
    resources->readIndex(&charKeySize, 0, 1);
    int key_size;
    for (std::fstream::pos_type i = 0;
         i < resources->getIndexFileLength();
         i += key_size + 1 + POINTER_LENGTH_IN_INDEX) {
        // in loop
        key_size = int(charKeySize);
        char *key = new char[key_size];
        char *value = new char[POINTER_LENGTH_IN_INDEX];
        resources->readIndex(key,
                             static_cast<size_t>(i) + 1,
                             static_cast<size_t>(key_size));
        resources->readIndex(value,
                             static_cast<size_t>(i) + 1 + key_size,
                             POINTER_LENGTH_IN_INDEX);
        auto polarString = new polar_race::PolarString(key);
        treeMap->add(polarString, Convert::charArrayToLong(value));
        if (key_size > 0) {
            delete[](key);
        }
        delete[](value);
        resources->readIndex(&charKeySize,
                             static_cast<size_t>(i) + 1 + key_size + POINTER_LENGTH_IN_INDEX,
                             1);
    }
    std::cout << "Memory Rebuild Success!" << std::endl;
}