package com.software.jetpack.compose.chan_xin_android.ext

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import java.lang.IllegalStateException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified VB:ViewBinding> Fragment.invokeViewBinding() = FragmentInflateBindingProperty(VB::class.java)

class FragmentInflateBindingProperty<VB:ViewBinding>(private val clazz: Class<VB>) :ReadOnlyProperty<Fragment,VB>{
    private var binding:VB? = null
    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (binding == null) {
            try {
                binding = (clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null,thisRef.layoutInflater) as VB)
            }catch (e: IllegalStateException){
                e.printStackTrace()
                throw e
            }
            thisRef.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    binding = null
                }
            })
        }
        return binding!!
    }

}
