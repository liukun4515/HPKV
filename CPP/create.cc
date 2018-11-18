//
// Created by wangxinshuo on 11/17/18.
//


#include "create.h"

void create::CreateDirectory(const char *name) {
    if (mkdir(name, S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH) == -1) {
        if (errno == EEXIST) {
            // already exists
        } else {
            // something else
            std::cout << "create error:" << strerror(errno) << std::endl;
        }
    }
}

void create::CreateFile(const char *name) {
    fclose(fopen(name, "a+"));
}