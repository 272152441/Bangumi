package com.zxwork.bmg.data.di

import com.zxwork.bmg.data.repository.CalendarRepository
import com.zxwork.bmg.data.repository.CalendarRepositoryImpl
import com.zxwork.bmg.data.repository.SubjectRepository
import com.zxwork.bmg.data.repository.SubjectRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCalendarRepository(
        calendarRepositoryImpl: CalendarRepositoryImpl
    ): CalendarRepository

    @Binds
    @Singleton
    abstract fun bindSubjectRepository(
        subjectRepositoryImpl: SubjectRepositoryImpl
    ): SubjectRepository
}
