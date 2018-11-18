//
// Created by wangxinshuo on 11/17/18.
//

#include "Hash.h"

int Hash::hash(std::string &key) {
    std::hash<std::string> str_hash;
    int result = static_cast<int>(str_hash(key));
    if(result >= 0)
        return result;
    return -result;
}

int Hash::hash(const char *key,size_t len) {
    std::string str(key,len);
    return hash(str);
}