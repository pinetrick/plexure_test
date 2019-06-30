package com.blueberrysolution.pinelib19.addone.inject_replace

import android.view.View
import com.blueberrysolution.pinelib19.R

import com.blueberrysolution.pinelib19.addone.ui_inject.interfaces.InjectView
import com.blueberrysolution.pinelib19.debug.G
import com.blueberrysolution.pinelib19.activity.A
import com.blueberrysolution.pinelib19.activity.T
import java.lang.ref.WeakReference
import kotlin.reflect.KClass


/*
用法

初始化调用

var holder = MyViewHolder.i<BusLocationViewHolder>(convertView, R.layout.bus_location_list_item)

类

class AdapterViewHolder(view: View): MyViewHolder(view) {
    val route_number: TextView? = null;
}

holder.route_number!!.text = "123"
 */
open class MyViewHolder(v: View){
    var view: WeakReference<View>;

    init{

        this.view = WeakReference<View>(v)
        reflect()

    }

    fun reflect(){
        var fields = this.javaClass.declaredFields;
        for (field in fields) {
            if (field.name == "view") continue;
            if (field.name.contains("serial") && field.name.contains("ersion")) continue; //version
            if (field.name[0] == '$') continue;

            try {
                val this_resource_id = A.a().getResources().getIdentifier(field.name, "id", A.a().getPackageName())
                var view = view.get()!!.findViewById(this_resource_id) as View

                field.isAccessible = true
                field.set(this, view)

                //G.d("Adapter加载 - " + field.name)

            } catch (e: Exception) {
                G.e(field.toGenericString() + "赋值失败 - " + e.toString())
            }

        }

    }



    companion object {
        public inline fun<reified T: MyViewHolder> i(lastView: View?, r_layout_id: Int): T {
            var holder: T;
            var v = lastView
            if (v == null){
                v = A.v(r_layout_id)

                val c1 = T::class.java.getConstructor(View::class.java)
                c1.setAccessible(true)
                holder = c1.newInstance(v)
                v.tag = holder
            }
            else{
                holder = v.tag as T
            }

            return holder
        }
    }
}