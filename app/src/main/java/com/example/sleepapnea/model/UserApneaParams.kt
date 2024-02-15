package com.example.sleepapnea.model

data class UserApneaParams(var obesise:Boolean=false,
                           var dayTimeSpleepiness:Boolean=false,
                           var hypertension:Boolean=false,
                           var physicalExcersise:Boolean=false,
                           var smoking:Boolean=false,
                           var snoring: Int=0,
                           var Drowsiness:Int=0,
                           var lifestyleScore:Int=0)