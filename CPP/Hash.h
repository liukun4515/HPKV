//
// Created by wangxinshuo on 11/17/18.
//

#ifndef KV_HASH_H
#define KV_HASH_H


#include <string>

class Hash {
public:
    static int hash(std::string&);

    static int hash(const char*,size_t);
};


#endif //KV_HASH_H
