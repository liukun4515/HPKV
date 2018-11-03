//
// Created by wangxinshuo on 11/1/18.
//

#include "Utils.h"

void write(const char *file_name, const char *data) {
    FILE *file;
    file = fopen(file_name, "a+");
    size_t len = strlen(data);
    fwrite(data, sizeof(char), len * (sizeof(char)), file);
    fclose(file);
}

void read(const char *file_name, size_t start, char *data) {
    FILE *file;
    file = fopen(file_name, "r");
    size_t len = strlen(data);
    fseek(file, start, SEEK_SET);
    fread(data, sizeof(char), len, file);
    fclose(file);
}

void createDirectory(const std::string &name) {
    if (mkdir(name.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH) == -1) {
        if (errno == EEXIST) {
            // already exists
        } else {
            // something else
            std::cout << "Create error:" << strerror(errno) << std::endl;
        }
    }
//    std::fstream index;
//    index.open(name + "/index.db",std::fstream::out);
//    index.close();
//    std::fstream store;
//    store.open(name + "/store.db",std::fstream::out);
//    store.close();
}


std::fstream::pos_type fileLength(const char *file_name) {
    std::ifstream in(file_name, std::ifstream::ate | std::ifstream::binary);
    return in.tellg();
}


void restore(const std::string &name, TreeMap *treeMap) {
    std::cout<<"File Length:"<<fileLength(name.data())<<std::endl;
    for (std::fstream::pos_type i = 0; i < fileLength(name.data()); i += DATA_SIZE) {
        char *key = new char[DATA_SIZE / 2];
        char *value = new char[DATA_SIZE / 2];
        read(name.data(), static_cast<size_t>(i), key);
        read(name.data(), static_cast<size_t>(i ) + DATA_SIZE / 2, value);
        treeMap->add(new polar_race::PolarString(key), charArrayToPosition(value));
        delete[](key);
        delete[](value);
    }
    std::cout<<"Memory Rebuild Success!"<<std::endl;
}


void positionToCharArray(long long position, char *data) {
    int count = 7;
    while (count != -1) {
        *(data + count) = (char) (position % 256);
        position = position >> 8;
        count--;
    }
}


long long charArrayToPosition(const char *data) {
    int count = 0;
    unsigned long long result = 0;
    while (count != 8) {
        result = result << 8;
        result += int(*(data + count));
        count++;
    }
    return result;
}
