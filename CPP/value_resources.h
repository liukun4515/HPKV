//
// Created by wangxinshuo on 11/17/18.
//

#ifndef KV_VALUERESOURCES_H
#define KV_VALUERESOURCES_H

#include <iostream>
#include <cstdio>
#include <string>
#include <mutex>

#include "conf.h"
#include "Hash.h"

class ValueResources {
public:
    explicit ValueResources(const char *);

    long long write(const char *, const char *);

    void read(const char *, char *, long long);

    ~ValueResources();

private:
    FILE *file[WRITE_RESOURCES_NUMBER];
    std::mutex resources_lock[WRITE_RESOURCES_NUMBER];
};


#endif //KV_VALUERESOURCES_H
