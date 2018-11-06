//
// Created by wangxinshuo on 11/1/18.
//

#ifndef KV_TREEMAP_H
#define KV_TREEMAP_H

#include <map>
#include <iostream>
#include "include/polar_string.h"

class TreeMap {
public:
    void add(polar_race::PolarString *, long long);

    long long get(polar_race::PolarString *);

    void close();

private:
    std::map<polar_race::PolarString, long long> data;
    std::map<polar_race::PolarString, long long>::iterator iter;
};


#endif //KV_TREEMAP_H
