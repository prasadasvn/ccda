package com.ids.ccda.mixins

class RequireMapFields {

    public static final DEFAULT_BLANK_VALUE = ""

    ArrayList<LinkedHashMap>  requireForMaps( ArrayList<LinkedHashMap> arrayOfMaps, ArrayList requiredFieldsAry, defaultValue = DEFAULT_BLANK_VALUE ){
        arrayOfMaps = arrayOfMaps?: []
        def ary = []
        arrayOfMaps.each{ LinkedHashMap map -> ary << requireForMap(map, requiredFieldsAry, defaultValue) }
        return ary
    }

  LinkedHashMap  requireForMap(  LinkedHashMap map,ArrayList requiredFieldsAry, defaultValue = DEFAULT_BLANK_VALUE ){
       map = map ?: [:]
       requiredFieldsAry = requiredFieldsAry?: []

       def keysToAdd = requiredFieldsAry - map.keySet()
       keysToAdd.each {key -> map[key] = defaultValue }
       return map
    }
}
