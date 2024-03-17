package com.example.cs492_finalproject_rxwatch.utils

enum class OutcomesEnum(val value: Int) {
    RECOVERED_RESOLVED(1),
    RECOVERING_RESOLVING(2),
    NOT_RECOVERED_OR_RESOLVED(3),
    RECOVERED_WITH_LONG_TERM_ISSUES(4),
    FATAL(5),
    UNKNOWN(6)
}