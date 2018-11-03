// Copyright [2018] Alibaba Cloud All rights reserved
#ifndef ENGINE_RACE_ENGINE_RACE_H_
#define ENGINE_RACE_ENGINE_RACE_H_

#include <string>
#include <mutex>
#include "include/engine.h"
#include "TreeMap.h"
#include "Utils.h"

namespace polar_race {

    class EngineRace : public Engine {
    public:
        static RetCode Open(const std::string &name, Engine **eptr);

        static TreeMap *treeMap;

        static std::string file_name;

        explicit EngineRace(const std::string &dir) {
        }

        ~EngineRace() override;

        RetCode Write(const PolarString &key,
                      const PolarString &value) override;

        RetCode Read(const PolarString &key,
                     std::string *value) override;

        /*
         * NOTICE: Implement 'Range' data quarter-final,
         *         you can skip it data preliminary.
         */
        RetCode Range(const PolarString &lower,
                      const PolarString &upper,
                      Visitor &visitor) override;

    private:

    };

}  // namespace polar_race

#endif  // ENGINE_RACE_ENGINE_RACE_H_
