import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mad2.R

class ClothesAdapter(private var clothesList: List<Cloth>) : RecyclerView.Adapter<ClothesAdapter.ClothesViewHolder>() {

    class ClothesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clothesImage: ImageView = itemView.findViewById(R.id.clothesImage)
        val clothesName: TextView = itemView.findViewById(R.id.clothesName)
        val clothesPrice: TextView = itemView.findViewById(R.id.clothesPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_clothes, parent, false)
        return ClothesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClothesViewHolder, position: Int) {
        val currentItem = clothesList[position]
        holder.clothesName.text = currentItem.name
        holder.clothesPrice.text = String.format("%.2f", currentItem.price)
        // Use Glide or another image loading library to load images from URLs
        Glide.with(holder.itemView.context)
            .load(currentItem.image)
            .into(holder.clothesImage)
    }

    override fun getItemCount() = clothesList.size

    fun updateData(newClothesList: List<Cloth>) {
        clothesList = newClothesList
        notifyDataSetChanged()
    }
}

data class Cloth(
    val name: String,
    val price: Double,
    val image: String,
    val description: String,
    val category: String
)

