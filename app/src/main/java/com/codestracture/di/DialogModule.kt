package com.codestracture.di

import android.content.Context
import com.codestracture.data.dialog.IDialog
import com.codestracture.data.dialog.IDialogImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@InstallIn(ActivityComponent::class)
@Module
class DialogModule {

    @Provides
    fun provideIDialog(@ActivityContext context: Context): IDialog {
        return IDialogImpl(context)
    }
}