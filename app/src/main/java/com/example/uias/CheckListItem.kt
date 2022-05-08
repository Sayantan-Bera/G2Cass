package com.example.uias

import com.google.firebase.database.Exclude

data class CheckListItem(
    @get:Exclude
    var id:String?=null,
    var checkIt:String?=null,
    @get:Exclude
    var isDeleted:Boolean=false
){
    override fun equals(other: Any?): Boolean {
        return if(other is CheckListItem){
            other.id==id
        }else false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (checkIt?.hashCode() ?: 0)
        result = 31 * result + isDeleted.hashCode()
        return result
    }
}
