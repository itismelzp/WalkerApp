package com.demo.logger

/**
 * Created by lizhiping on 2023/4/2.
 * <p>
 * description
 */
object BatchOpDBUtil {

    /**
     * 本例是根据guid字段更新数据表中的 value1，updateTime以及uploadFlag三个字段的值，他们的值是不一样的
     * 采用 case when 语法一次新更新多条数据，而不是一条一条的去更新
     *
     * @param dataRecordTables 需要更新的数据
     */
    @JvmStatic
    fun update(dataRecordTables: List<DataRecordTable>): String {
        var value1Str = ""
        var updateTimeStr = ""
        var updateFlagStr = ""
        var guids = ""
        guids += "( '"
        value1Str += " SET value1 = CASE guid " //需要更新的字段 value1
        updateTimeStr += "  updateTime = CASE guid " //需要更新的字段 updateTime
        updateFlagStr += "  uploadFlag = CASE guid " //需要更新的字段 uploadFlag
        for (i in dataRecordTables.indices) {
            val data: DataRecordTable = dataRecordTables[i]
            if (i == dataRecordTables.size - 1) {
                value1Str += " WHEN '${data.guid}' THEN '${data.value1}' \n End,"
                updateTimeStr += " WHEN '${data.guid}' THEN '${data.updateTime}' \n End,"
                updateFlagStr += " WHEN '${data.guid}' THEN '${data.uploadFlag}' \n End "
                guids += data.guid + "')"
            } else {
                value1Str += " WHEN '${data.guid}' THEN '${data.value1}' \n "
                updateTimeStr += " WHEN '${data.guid}' THEN '${data.updateTime}' \n "
                updateFlagStr += " WHEN '${data.guid}' THEN '${data.uploadFlag}' \n "
                guids += "${data.guid}','"
            }
        }

        return "Update ${getTableName()} $value1Str$updateTimeStr$updateFlagStr WHERE guid in $guids"

//        MySqlOpenHelper.getmDatabase().execSQL(sql)
    }

    private fun getTableName(): String {
        return "table"
    }
}