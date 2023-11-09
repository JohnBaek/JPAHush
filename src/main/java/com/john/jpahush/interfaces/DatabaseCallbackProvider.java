package com.john.jpahush.interfaces;

import com.john.jpahush.data.commondata.responses.QueryResults;

import java.util.Date;

public class DatabaseCallbackProvider implements IDatabaseCallbackProvider{
    @Override
    public <T> void afterCreatedQueryResultsCallback(String name, boolean isOnlyProjections, int skip, int countPerPage, String[] orderFields, String[] orderDirections, String[] searchFields, String[] searchValues, String[] searchPeriodFields, Date[] searchStartDates, Date[] searchEndDates, QueryResults<T> queryResults, Class clazz) {

    }
}
