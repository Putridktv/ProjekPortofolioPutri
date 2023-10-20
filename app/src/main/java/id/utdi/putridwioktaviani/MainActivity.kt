package id.utdi.putridwioktaviani

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import id.utdi.putridwioktaviani.data.Datasource
import id.utdi.putridwioktaviani.model.Affirmation
import id.utdi.putridwioktaviani.ui.theme.ProjekPortofolioPutriTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjekPortofolioPutriTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProjekPortofolioPutriApp() //memanggil fungsi ProjekPortofolioPutriApp untuk dijalankan
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjekPortofolioPutriApp(){ //Membuat fungsi ProjekPortofolioApp
    var selectedItem by remember { mutableStateOf<Affirmation?>(null) } // Menambahkan state untuk item card list portofolio yang dipilih

    Scaffold (
        topBar = {  //digunakan untuk membuat bagian header dengan teks Portofolio/ top bar
            CenterAlignedTopAppBar(
                title = {
                    Text( //bagian untuk membuat teks top bar
                        text = "Portofolio",
                        fontWeight = FontWeight.Bold // agar font bold
                    )
                }, //bagian modifier untuuk mengatur tampilan top bar
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ){ innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn( //menggunakan lazycolumn untuk menampilkan scrollable list kebawah
                modifier = Modifier.padding(innerPadding)
            ) {
                items(Datasource().loadAffirmations()) { affirmation ->
                    AffirmationCard( //memanggil fungsi AffirmationCard agar menjadi scrollable menggunakan lazycolumn
                        affirmation = affirmation,
                        onCardClick = { affirmation -> //berfungsi untuk merujuk data yang akan ditampilkan setelah meng-klik card item list berdasarkan data affirmation
                            selectedItem = affirmation
                        }
                    )
                }
            }
            selectedItem?.let { item -> //berfungsi untuk menampilkan DetailDialog setelah mengklik card sesuai dengan data yang di klik
                DetailDialog(affirmation = item, onClose = { selectedItem = null })
            }
        }
    }
}

@Composable //fungsi untuk membuat tampilan tiap item card
fun AffirmationCard(affirmation: Affirmation, onCardClick: (Affirmation) -> Unit) {
    Card( //menggunakan card agar mempercepat pembuatan shape column
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCardClick(affirmation) }
    ) {
        Row {//menggunakan row agar gambar dengan teks sejajar
            Image(
                painter = painterResource(affirmation.imageResourceId), //memanggil gambar berdasarkan imageResourceId dalam Datasource
                contentDescription = stringResource(affirmation.stringResourceId), //memanggil teks berdasarkan stringResourceId dalam Datasource
                modifier = Modifier //menggunakna modifier unntuk mengatur tampilan image
                    .size(100.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.FillHeight
            )
            Column ( //menggunakan column agar teks dapat urut kebawah
                modifier = Modifier
                    .align(Alignment.CenterVertically) // Memusatkan teks secara center /tengah vertikal
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(affirmation.stringResourceId), //memanggil teks berdasarkan stringResourceId dalam datasource
                    style = MaterialTheme.typography.headlineSmall, //mengatur ukuran teks
                    textAlign = TextAlign.Center //mengatur teks agar rata tengah
                )

                Text(
                    text = stringResource(affirmation.stringCreatorResourceId), //memanggil teks berdasarkan stringResourceId dalam datasource
                    style = MaterialTheme.typography.bodyLarge, //mengatur ukuran teks
                    textAlign = TextAlign.Center //mengatur teks agar rata tengah
                )
            }
        }
    }
}

@Composable
fun UserCard(modifier: Modifier = Modifier){ //function untuk bagain informasi penulis artikel
    Row( //menggunakan row agar gambar dan teks dapa sejajar
        verticalAlignment = Alignment.CenterVertically, //untuk men-centerkan antara teks dengan gambar
        modifier = Modifier
            .padding(10.dp)
    ) {
        Box {//mengngunakan box agar mudah mengatur posisi gambar profile picture dengan icon centang yang dapat diibaratkan digabungkan dalam satu area
            Image(
                //untuk menambahkan gambar dengan nama file "profpic"
                painter = painterResource(R.drawable.profpic),
                contentDescription = "Profile picture",
                modifier = Modifier
                    // Set ukuran gambar menjadi 50 dp
                    .size(50.dp)
                    // set gambar dari kotak menjadi lingkaran
                    .clip(CircleShape)
                //menambahkan desain material
//                .border(1.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            )
            Icon(
                painter = painterResource(R.drawable.centang), //menambahkan gambar icon dengan nama file "centang"
                contentDescription = null,
                modifier = Modifier
                    .size(15.dp) //mengatur ukuran gambar
                    .align(Alignment.BottomEnd) //mengatur letak gambar berada di bawah akhir
            )
        }

        Spacer(modifier = Modifier.width(8.dp)) //menambahkan jarak antara gambar dengan teks

        Column {//mengguakan kolon agarr tulisan nama dan tanggal dapat rapi berurut kebawah
            Text(
                text = "Putri Dwi Oktaviani",
                fontWeight = FontWeight.Bold //menebalkan tulisan

            )

            Spacer(modifier = Modifier.width(10.dp)) //menambahkan jarak antar teks nama dan tanggal

            Text( //menambahkan teks tanggal
                text = "29 Sept 2023",
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun DetailDialog(affirmation: Affirmation, onClose: () -> Unit) { //membuat funggsi DetailDialog untuk action setelah card item di klik
    Dialog( //Menggunakan dialog agar menampilkan pop up yang akan diisikan detail dari card yang di klik
        onDismissRequest = { onClose() },
        content = {
            Card () {//menggunakan card agar mempercepat pembuatan shape column
                Column( //mengguakan column agar fungsi, gambar dan teks  dapat rapi tertampil berurut kebawah
                    modifier = Modifier
                        .background(color = Color.White) //mengubah background pop up menjadi warna putih
                        .verticalScroll(rememberScrollState()) //menggunakan verticalScroll agar isi dari pop up dialog dapat di scroll ketika mode landscape
//                       .border(1.dp, Color.DarkGray),
                ){
                    UserCard() //memanggil fungsi UserCard untuk ditampilkan dalam pop up dialog

                    Image(
                        painter = painterResource(affirmation.imageResourceId), //memanggil gambar berdasarkan imageResourceId dalam Datasource
                        contentDescription = stringResource(affirmation.stringResourceId), //memanggil teks berdasarkan stringResourceId dalam datasource
                        modifier = Modifier  //bagian modifier untuuk mengatur gambar
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )

                    Text(
                        text = stringResource(affirmation.stringResourceId), //memanggil teks berdasarkan stringResourceId dalam datasource
                        modifier = Modifier.padding(16.dp), //bagian modifier untuuk mengatur gambar
                        style = MaterialTheme.typography.bodyLarge, //mengatur ukuran teks
                        textAlign = TextAlign.Center //untuk membuat teks ditengah
                    )

                    Text( //digunakan utnuk menampilkan teks deskripsi portofolio
                        text = stringResource(affirmation.stringDetailResourceId), //memanggil teks berdasarkan stringDetailResourceId dalam datasource
                        style = MaterialTheme.typography.bodyMedium, //mengatur ukuran teks
                        textAlign = TextAlign.Justify, //untuk membuat teks rata kanan kiri
                        modifier = Modifier
                            .padding(15.dp) //menambahkan padding dengan modifier dalam teks deskripsi portofolio
                    )
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable //fungsi preview yang digunakan untuk melihat hasil tanpa menggunakna emulator
fun ProjekPortofolioPutriPreview() {
    ProjekPortofolioPutriTheme {
        ProjekPortofolioPutriApp() //memanggil fungsi ProjekPortofolioApp untuk ditampilkan dalam fungsi preview
    }
}