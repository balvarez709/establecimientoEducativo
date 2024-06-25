import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.establecimientoeducativo.R

class CustomSpinnerAdapter(
    context: Context,
    private val texts: Array<String>
) : ArrayAdapter<String>(context, R.layout.spinner_item, texts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent, R.layout.spinner_item)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent, R.layout.spinner_dropdown_item)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup, layoutResource: Int): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)
        val textView: TextView = view.findViewById(R.id.text)

        textView.text = texts[position]

        if (layoutResource == R.layout.spinner_item) {
            val imageView: ImageView = view.findViewById(R.id.dropdown_icon)
            imageView.setImageResource(R.drawable.ic_arrow_drop_down)
        }

        return view
    }
}
