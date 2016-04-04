package com.kotlinchina.smallpockets.service.impl

import java.util.*

class CalendarService {

    fun getMondayAndSundayCalendarOfThisWeek(calendar: Calendar): Pair<Calendar, Calendar> {
        val mondayCalendar = getMondayCalendar(calendar)
        val sundayCalendar = getSundayCalendar(calendar)
        return Pair(mondayCalendar, sundayCalendar)
    }

    fun getMondayAndSundayDateOfThisWeek(calendar: Calendar): Pair<Date, Date> {
        val datePair = getMondayAndSundayCalendarOfThisWeek(calendar)
        return Pair(calendarToDate(datePair.first), calendarToDate(datePair.second))
    }

    fun getMondayAndSundayDateOfThisWeek(date: Date): Pair<Date, Date> {
        return getMondayAndSundayDateOfThisWeek(dateToCalendar(date))
    }

    private fun getMondayCalendar(calendar: Calendar): Calendar {
        var newCalendar = getNewCalendar(calendar)

        val currentDayOfWeek = getDayOfWeek(newCalendar)
        newCalendar.add(Calendar.DAY_OF_YEAR, -currentDayOfWeek + 1)
        newCalendar.set(Calendar.HOUR, 0)
        newCalendar.set(Calendar.MINUTE, 0)
        newCalendar.set(Calendar.SECOND, 0)
        return newCalendar
    }

    private fun getSundayCalendar(calendar: Calendar): Calendar {
        val newCalendar = getNewCalendar(calendar)
        val currentDayOfWeek = getDayOfWeek(newCalendar)
        newCalendar.add(Calendar.DAY_OF_YEAR, 7 - currentDayOfWeek)
        newCalendar.set(Calendar.HOUR, 23)
        newCalendar.set(Calendar.MINUTE, 59)
        newCalendar.set(Calendar.SECOND, 59)
        return newCalendar
    }

    private fun getDayOfWeek(calendar: Calendar): Int {
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (currentDayOfWeek == 0) {
            return 7
        } else {
            return currentDayOfWeek
        }
    }

    private fun getNewCalendar(calendar: Calendar): Calendar {
        var newCalendar = Calendar.getInstance()
        newCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
        return newCalendar
    }

    fun dateToCalendar(date: Date): Calendar {
        var calendar = Calendar.getInstance()
        calendar.time = date
        return calendar

    }

    fun calendarToDate(calendar: Calendar): Date {
        return calendar.time
    }
}