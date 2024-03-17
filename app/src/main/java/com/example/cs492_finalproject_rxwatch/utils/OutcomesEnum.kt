package com.example.cs492_finalproject_rxwatch.utils

/*
 * This enum maps the values from their API values 1-6 to more readable
 * and understandable names, hopefully creating more readable and understandable
 * code.
 */
enum class OutcomesEnum(val value: Int) {
    RECOVERED_RESOLVED(1),
    RECOVERING_RESOLVING(2),
    NOT_RECOVERED_OR_RESOLVED(3),
    RECOVERED_WITH_LONG_TERM_ISSUES(4),
    FATAL(5),
    UNKNOWN(6)
}