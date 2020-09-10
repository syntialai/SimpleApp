package com.blibli.simpleapp.core.di.module

import androidx.lifecycle.ViewModel
import com.blibli.simpleapp.core.di.ViewModelKey
import com.blibli.simpleapp.feature.user.viewmodel.DetailViewModel
import com.blibli.simpleapp.feature.user.viewmodel.MainViewModel
import com.blibli.simpleapp.feature.user.viewmodel.UserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [UserModule::class])
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(detailViewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel
}