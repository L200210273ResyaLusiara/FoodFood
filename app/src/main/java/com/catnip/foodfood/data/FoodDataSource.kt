package com.catnip.foodfood.data

import com.catnip.foodfood.model.Food

interface FoodDataSource {
    fun getFoods(): List<Food>
}

class FoodDataSourceImpl() : FoodDataSource {
    override fun getFoods(): List<Food> = listOf(
        Food(
            "Ayam Goreng Kriukaa",
            "Rp 15000",
            "https://raw.githubusercontent.com/L200210273ResyaLusiara/FoodFood/push_asset/images/img_ayam_goreng1.jpg",
            "https://www.google.com/maps?q=-6.2088,106.8450",
            "Ayam goreng adalah hidangan yang terdiri dari potongan ayam yang dicelupkan dalam adonan atau rempah-rempah, kemudian digoreng hingga kulitnya renyah dan dagingnya empuk. Ayam goreng sering disajikan dengan saus atau rempah khusus untuk memberikan rasa yang lezat."
        ),
        Food(
            "Dim Sum",
            "Rp 10000",
            "https://raw.githubusercontent.com/L200210273ResyaLusiara/FoodFood/push_asset/images/img_dimsum2.jpg",
            "https://www.google.com/maps?q=-6.2088,106.8450",
            " Dimsum adalah hidangan tradisional Cina yang terdiri dari berbagai jenis makanan kecil seperti dumpling, pangsit, bao, dan hidangan lainnya. Dimsum biasanya disajikan dalam keranjang bambu dan sering dihidangkan sebagai makanan ringan atau sarapan."
        ),
        Food(
            "Mocca Coffee",
            "Rp 15000",
            "https://raw.githubusercontent.com/L200210273ResyaLusiara/FoodFood/push_asset/images/img_coffe1.jpg",
            "https://www.google.com/maps?q=-6.2088,106.8450",
            "Mocha coffee adalah minuman kopi yang terbuat dari campuran espresso, susu, dan cokelat. Ini adalah minuman yang kaya dan beraroma, dengan rasa kopi yang kuat dicampur dengan manisnya cokelat."
        ),
        Food(
            "Mie Setan",
            "Rp 10000",
            "https://raw.githubusercontent.com/L200210273ResyaLusiara/FoodFood/push_asset/images/img_image1.jpg",
            "https://www.google.com/maps?q=-6.2088,106.8450",
            "Pizza adalah hidangan Italia yang sangat terkenal di seluruh dunia. Ini terdiri dari adonan tipis yang dilapis dengan saus tomat, keju, dan berbagai macam topping seperti pepperoni, jamur, paprika, dan lainnya. Pizza bisa disesuaikan sesuai selera dan dimakan baik dalam bentuk potongan maupun utuh."
        ),
        Food(
            "Pizza Sosis",
            "Rp 28000",
            "https://raw.githubusercontent.com/L200210273ResyaLusiara/FoodFood/push_asset/images/img_pizza.jpg",
            "https://www.google.com/maps?q=-6.2088,106.8450",
            "Mie Setan adalah hidangan mie pedas yang populer di Indonesia. Mie ini biasanya dimasak dengan berbagai rempah-rempah pedas dan saus, sering kali menggunakan cabai sebagai bahan utama. Mie Setan sering dihiasi dengan telur, sayuran, dan daging sesuai selera."
        ),
        Food(
            "Ayam Goreng Chiki",
            "Rp 12000",
            "https://raw.githubusercontent.com/L200210273ResyaLusiara/FoodFood/push_asset/images/img_ayam_goreng3.jpg",
            "https://www.google.com/maps?q=-6.2088,106.8450",
            "Ayam Goreng Chiky adalah variasi ayam goreng yang mungkin memiliki citarasa dan resep khusus dari suatu daerah atau restoran tertentu. Nama \"Chiky\" bisa saja mengacu pada nama tempat atau merek tertentu. Biasanya, ayam goreng Chiky akan memiliki karakteristik rasa dan tekstur yang unik sesuai dengan resep yang digunakan oleh tempat tersebut."
        )

    )

}