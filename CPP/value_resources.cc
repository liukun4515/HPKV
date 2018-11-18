//
// Created by wangxinshuo on 11/17/18.
//


#include "value_resources.h"

ValueResources::ValueResources(const char *key) {
    std::string dir(key);
    for (int i = 0; i < WRITE_RESOURCES_NUMBER; ++i) {
        std::string file_name(dir);
        file_name.append("/value.db.").append(std::to_string(i));
        this->file[i] = fopen(file_name.data(), "a+");
    }
}

long long ValueResources::write(const char *key, const char *value) {
    int index = Hash::hash(key, KEY_SIZE) % WRITE_RESOURCES_NUMBER;
    resources_lock[index].lock();
    fseek(this->file[index], 0L, SEEK_END);
    long long result = ftell(this->file[index]);
    fwrite(value, sizeof(char), VALUE_SIZE, this->file[index]);
    fflush(this->file[index]);
    resources_lock[index].unlock();
    return result;
}

void ValueResources::read(const char *key, char *value, long long start) {
    int index = Hash::hash(key, KEY_SIZE) % WRITE_RESOURCES_NUMBER;
    //resources_lock[index].lock();
    fseek(this->file[index], start, SEEK_SET);
    size_t result = fread(value, sizeof(char), VALUE_SIZE, this->file[index]);
    //resources_lock[index].unlock();
    if (VALUE_SIZE != result) {
        std::cout << "ErrorRead!" << std::endl;
    }
}

ValueResources::~ValueResources() {
    std::cout << "CloseValueResources!" << std::endl;
    for (auto &i : this->file) {
        fclose(i);
    }
}
