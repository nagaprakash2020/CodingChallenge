package com.learn.codingChallenge.repository

import javax.inject.Inject

class CodingRepo @Inject constructor(private val remoteRepo:RemoteRepo){
}