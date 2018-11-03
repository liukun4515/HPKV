//
// Created by wangxinshuo on 11/1/18.
//

#ifndef KV_UTILS_H
#define KV_UTILS_H

#define DATA_SIZE 16

#include <string>
#include <cstring>
#include <string.h>
#include <sys/stat.h>
#include <iostream>
#include <fstream>

#include "TreeMap.h"
#include "Utils.h"

void createDirectory(const std::string &);
void restore(const std::string &name, TreeMap *treeMap);
void write(const char* file_name, const char* data);
void read(const char* file_name, size_t start, char* data);
std::fstream::pos_type fileLength(const char *file_name);
void positionToCharArray(long long position, char *data);
long long charArrayToPosition(const char *data);

#endif //KV_UTILS_H
