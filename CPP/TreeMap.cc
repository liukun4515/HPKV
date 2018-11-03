//
// Created by wangxinshuo on 11/1/18.
//


#include "TreeMap.h"

void TreeMap::add(polar_race::PolarString *key,long long value) {
    this->data.insert(std::pair<polar_race::PolarString, long long>(*key, value));
}

long long TreeMap::get(polar_race::PolarString *key) {
    this->iter = this->data.find(*key);
    if (this->iter != this->data.end()) {
        return this->iter->second;
    }
    return -1;
}


void TreeMap::close(){
    data.clear();
}