package com.veon.prebid.demo.data.utils

sealed class AmiNoteException : Exception {
    constructor(message: String) : super(message)
    constructor() : super()
}

class DataConflict(message: String) : AmiNoteException(message)