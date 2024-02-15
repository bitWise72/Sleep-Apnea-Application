package com.example.sleepapnea.model

class ApneaParamBuilder {

    private lateinit var userApneaParams: UserApneaParams

    fun ApneaBuilder(){
        this.userApneaParams= UserApneaParams()
    }

    fun add_obesise(obesise:Boolean){
        userApneaParams.obesise=obesise
    }

    fun add_dayTimeSleepiness(dayTimeSleepiness:Boolean){
        userApneaParams.dayTimeSpleepiness=dayTimeSleepiness
    }

    fun add_hypertension(hypertension:Boolean){
        userApneaParams.hypertension=hypertension
    }

    fun add_smoking(smoking:Boolean){
        userApneaParams.smoking=smoking
    }

    fun add_physicalExcersise(physicalExcersise:Boolean){
        userApneaParams.physicalExcersise=physicalExcersise
    }

    fun add_snoring(snoring:Int){
        userApneaParams.snoring=snoring
    }

    fun add_drowsiness(drowsiness:Int){
        userApneaParams.Drowsiness=drowsiness
    }

    fun add_score(score:Int){
        userApneaParams.lifestyleScore=score
    }

    fun build():UserApneaParams{
        return this.userApneaParams
    }
}