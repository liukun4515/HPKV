#ifndef PREDEFINED_H
#define PREDEFINED_H

#include <string.h>

namespace bpt
{

/* predefined B+ info */
#define BP_ORDER 25
#define BPT_MOVE_STEP 8

/* key/value type */
typedef long long value_t;
struct key_t
{
    unsigned long long k = 0;
    key_t(const char *str = "")
    {
        char temp;
        for (char i = 0; i < BPT_MOVE_STEP; i++)
        {
            k <<= BPT_MOVE_STEP;
            temp = *(str + i);
            if (temp >= 0)
                k += temp;
            else
                k += 127 - temp;
        }
    }
};

inline int keycmp(const key_t &a, const key_t &b)
{
    if (a.k == b.k)
        return 0;
    if (a.k < b.k)
        return -1;
    return 1;
}

#define OPERATOR_KEYCMP(type)                      \
    bool operator<(const key_t &l, const type &r)  \
    {                                              \
        return keycmp(l, r.key) < 0;               \
    }                                              \
    bool operator<(const type &l, const key_t &r)  \
    {                                              \
        return keycmp(l.key, r) < 0;               \
    }                                              \
    bool operator==(const key_t &l, const type &r) \
    {                                              \
        return keycmp(l, r.key) == 0;              \
    }                                              \
    bool operator==(const type &l, const key_t &r) \
    {                                              \
        return keycmp(l.key, r) == 0;              \
    }

} // namespace bpt

#endif /* end of PREDEFINED_H */
