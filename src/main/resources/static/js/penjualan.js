// ====================MODUL - PENJUALAN KASIR===================


// 1. ====================MODUL CUSTOMER DAN MEMBER===================
// -----Element HTML - Customer
const sectionNonMember = document.getElementById("sectionNonMember");
const sectionMember = document.getElementById("sectionMember");
const txtNamaCustomer = document.getElementById("txtNamaCustomer");
const txtNoTelepon = document.getElementById("txtNoTelepon");
const idCustomerInput = document.getElementById("idCustomerTerpilih");
const btnResetMember = document.getElementById("btnResetMember");

// ------State Customer Aktif-----
let customerTerpilih = null;

// ------1. CARI MEMBER - MODAL PENCARIAN--------
document.getElementById("btnCariMember").addEventListener("click", function() {
    document.getElementById("modalCariMember").style.display = "flex";
    document.getElementById("inputanCariMember").focus();
});

// Proses pencarian - pengetikan nama
document.getElementById("inputanCariMember").addEventListener("input", function() {
    const keyword = this.value.toLowerCase();

    // Mengambil semua baris di dalam tbody tabelHasilCari
    const semuaRow = document.querySelectorAll("#tabelHasilCari tr");
    semuaRow.forEach(row => {
        const namaAttr = row.getAttribute("data-nama");
        if (namaAttr) { // Pastikan stiker atributnya ada dulu biar gak eror null
            const nama = namaAttr.toLowerCase();
            row.style.display = nama.includes(keyword) ? "" : "none";
        }
    });
});

// Tutup modal cari member
document.getElementById("btnTutupModalCari").addEventListener("click", function() {
    document.getElementById("modalCariMember").style.display = "none";
});

// Logic pilih hasil cari
function pilihMember(id, nama, telepon) {
    // Kita pastikan kalau telepon itu null/undefined, kita ganti jadi string kosong atau strip
    const telpBersih = (telepon === null || telepon === undefined || telepon === "null") ? "-" : telepon;

    customerTerpilih = {
        idCustomer: id, 
        namaCustomer: nama, 
        noTelepon: telpBersih
    };
    
    updateMemberUI();
    document.getElementById("modalCariMember").style.display = "none";
}

// 2. ---------------TAMBAH MEMBER - MODAL FORM------------
document.getElementById("btnTambahMember").addEventListener("click", function() {
    document.getElementById("modalTambahMember").style.display = "flex";
});

// Tutup Modal
document.getElementById("btnTutupModalTambah").addEventListener("click", function() {
    document.getElementById("modalTambahMember").style.display = "none";
});

// Submit form - tambah member baru
document.getElementById("btnSimpanMemberBaru").addEventListener("click", function() {
    const nama = document.getElementById("inputNamaBaru").value.trim();
    const telepon = document.getElementById("inputTelpBaru").value.trim() || "-";

    if(!nama) {
        alert("Nama member wajib diisi!");
        return;
    }

    // ----Pengiriman ke backend
    fetch("/customer/simpan-ajax", {
        method: "POST",
        headers: { "Content-Type" : "application/json" },
        // PASTIKAN KEY DI SINI SAMA PERSIS DENGAN VARIABLE DI CLASS JAVA CUSTOMER LO
        body: JSON.stringify({ 
            namaCustomer: nama, 
            noTelepon: telepon 
        })
    })
    .then(res => {
        if (!res.ok) {
            // Kalau eror, kita ambil pesan erornya dari server biar tau kenapa 400
            return res.text().then(text => { throw new Error(text) });
        }
        return res.json();
    })
    .then(data => {
        console.log("Data sukses:", data);

        customerTerpilih = {
            idCustomer: data.idCustomer || data.id,
            namaCustomer: data.namaCustomer || data.nama,
            noTelepon: data.noTelepon || data.telepon || "-"
        };
        
        updateMemberUI();
        
        // FIX: Tutup modal pakai cara yang sama (style.display) biar konsisten
        document.getElementById("modalTambahMember").style.display = "none";

        // Reset form
        document.getElementById("inputNamaBaru").value = "";
        document.getElementById("inputTelpBaru").value = "";
    })
    .catch((err) => {
        console.error("Eror:", err);
        alert("Gagal menyimpan data! Pesan eror: " + err.message);
    });
});

document.getElementById("tabelHasilCari").addEventListener("click", function(e) {
    if (e.target.classList.contains("btnPilihMember") || e.target.tagName === "BUTTON") {
        const row = e.target.closest("tr");
        if (row) {
            const id    = row.getAttribute("data-id") || row.getAttribute("th:data-id");
            const nama  = row.getAttribute("data-nama") || row.getAttribute("th:data-nama");
            const telp  = row.getAttribute("data-telp") || row.getAttribute("data-telepon");
            if (id && nama) {
                pilihMember(id, nama, telp);
            }
        }
    }
});

// 3.------------------RESET /HAPUS MEMBER--------------
btnResetMember.addEventListener("click", function() {
    customerTerpilih = null;
    idCustomerInput.value = "";
    sectionMember.style.display    = "none";
    sectionNonMember.style.display = "block";
    btnResetMember.style.display   = "none";
});



// 5. ------------------UPDATE TAMPILAN UI --------------
function updateMemberUI() {
    if (customerTerpilih) {
        txtNamaCustomer.innerText = customerTerpilih.namaCustomer;
        
        // PASTIIN INI NAMA VARIABLE NYA SAMA
        txtNoTelepon.innerText    = customerTerpilih.noTelepon; 

        idCustomerInput.value     = customerTerpilih.idCustomer;

        sectionNonMember.style.display = "none";
        sectionMember.style.display    = "block";
        btnResetMember.style.display   = "inline-block";
    }
}

// 6.-------------------FILTER MEMBER(saat cari)------------------
function filterMemberLokal() {
    var input = document.getElementById("inputanCariMember");
    var filter = input.value.toLowerCase();
    
    // Sinkronisasi: langsung menembak tr di dalam tabelHasilCari agar tidak bergantung pada nama class row-member
    var rows = document.querySelectorAll("#tabelHasilCari tr");
    
    for (var i = 0; i < rows.length; i++) {
        // Membaca attribute data-nama yang sudah di-render Thymeleaf sebelumnya
        var namaCustomer = rows[i].getAttribute("data-nama") || rows[i].getAttribute("th:data-nama") || "";
        if (namaCustomer.toLowerCase().indexOf(filter) > -1) {
            rows[i].style.display = ""; // Baris tampil jika cocok
        } else {
            rows[i].style.display = "none"; // Baris sembunyi jika tidak cocok
        }
    }
}








// ==================== MODUL 2 - SISTEM KERANJANG BELANJA ===================
// 1.Element HTML - 

// State Array untuk menampung item yang sedang dibeli
let keranjangBelanja = [];

// Fungsi Utama: Tambah Barang Berdasarkan Kode
function tambahBarangByKode() {
    const inputKode = document.getElementById("inputKodeBarang");
    const kodeCari = inputKode.value.trim().toLowerCase();

    if (kodeCari === "") {
        alert("Ketik kode barang terlebih dahulu!");
        return;
    }

    // 1. Cari data barang di elemen tersembunyi HTML yang di-render Thymeleaf
    const rowsMaster = document.querySelectorAll("#dataMasterBarangTersembunyi table tr");
    let barangDitemukan = null;

    for (let row of rowsMaster) {
        let kodeBarang = row.getAttribute("data-kode").toLowerCase();
        if (kodeBarang === kodeCari) {
            barangDitemukan = {
                idBarang: parseInt(row.getAttribute("data-id")),
                kodeBarang: row.getAttribute("data-kode"),
                namaBarang: row.getAttribute("data-nama"),
                hargaJual: parseFloat(row.getAttribute("data-harga")),
                brand: row.getAttribute("data-brand"),
                stok: parseInt(row.getAttribute("data-stok"))
            };
            break;
        }
    }

    // 2. Jika barang TIDAK ditemukan, munculkan pesan peringatan
    if (!barangDitemukan) {
        alert("Barang dengan kode '" + inputKode.value + "' tidak ditemukan!");
        inputKode.value = "";
        inputKode.focus();
        return;
    }

    // 3. Cek apakah barang tersebut SUDAH ADA di dalam keranjang belanja
    const itemEksis = keranjangBelanja.find(item => item.idBarang === barangDitemukan.idBarang);

    if (itemEksis) {
        // Cek batasan stok sebelum menambah Qty
        if (itemEksis.qty + 1 > barangDitemukan.stok) {
            alert("Stok tidak mencukupi! Stok maksimal tersisa: " + barangDitemukan.stok);
            return;
        }
        itemEksis.qty += 1;
    } else {
        // Cek apakah stok barang master emang sudah 0 dari awal
        if (barangDitemukan.stok < 1) {
            alert("Stok barang ini kosong!");
            return;
        }
        // Jika belum ada, masukkan data baru dengan default Qty = 1 dan Diskon = 0
        keranjangBelanja.push({
            idBarang: barangDitemukan.idBarang,
            kodeBarang: barangDitemukan.kodeBarang,
            namaBarang: barangDitemukan.namaBarang,
            hargaJual: barangDitemukan.hargaJual,
            brand: barangDitemukan.brand,
            stokMax: barangDitemukan.stok,
            qty: 1,
            diskonPersen: 0
        });
    }

    // 4. Reset inputan dan gambar ulang tabel keranjang
    inputKode.value = "";
    renderTabelKeranjang();
    inputKode.focus();
}

// Fungsi untuk Menggambar Ulang Tabel Keranjang Belanja secara Dinamis
function renderTabelKeranjang() {
    const tbody = document.getElementById("tabelKeranjang");
    tbody.innerHTML = ""; // Bersihkan tabel terlebih dahulu

    // Jika keranjang kosong, tampilkan baris default "Keranjang Kosong"
    if (keranjangBelanja.length === 0) {
        tbody.innerHTML = `
            <tr id="rowKosong">
                <td colspan="7" style="text-align: center; color: #999; padding: 20px;">Keranjang masih kosong. Ketik Kode Barang!</td>
            </tr>
        `;
        document.getElementById("jsonKeranjang").value = "[]";
        updateGrandTotalUI(0);
        return;
    }

    let grandTotal = 0;

    // Looping data array untuk digambar ke dalam baris tabel HTML
    keranjangBelanja.forEach((item, index) => {
        // Hitung subtotal sementara di sisi client: (Harga * Qty) - Diskon Potongan Harga
        let totalKotor = item.hargaJual * item.qty;
        let potonganDiskon = totalKotor * (item.diskonPersen / 100);
        let subtotal = totalKotor - potonganDiskon;
        
        grandTotal += subtotal;

        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td><b>${item.namaBarang}</b> <br><small style="color:#666">${item.kodeBarang}</small></td>
            <td>Rp ${item.hargaJual.toLocaleString('id-ID')}</td>
            <td>
                <input type="number" value="${item.qty}" min="1" max="${item.stokMax}" 
                    style="width: 60px; text-align: center;" 
                    onchange="updateQtyKeranjang(${index}, this.value)">
                <br><small style="color: #22c55e">Sisa: ${item.stokMax}</small>
            </td>
            <td>${item.brand}</td>
            <td>
                <input type="number" value="${item.diskonPersen}" min="0" max="100" 
                    style="width: 50px; text-align: center;" 
                    onchange="updateDiskonKeranjang(${index}, this.value)"> %
            </td>
            <td><b>Rp ${subtotal.toLocaleString('id-ID')}</b></td>
            <td>
                <button type="button" onclick="hapusItemKeranjang(${index})" 
                    style="background: #ef4444; color: white; border: none; padding: 4px 8px; border-radius: 4px; cursor: pointer;">
                    Hapus
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });

    // 5. Bungkus data array menjadi JSON String agar bisa dibaca saat Submit Form ke Java
    document.getElementById("jsonKeranjang").value = JSON.stringify(keranjangBelanja);
    
    // Update total pembayaran global di UI kasir lu
    updateGrandTotalUI(grandTotal);
}

// Fungsi Pemicu saat Kasir mengubah Angka Qty di Tabel
function updateQtyKeranjang(index, nilaiBaru) {
    let qty = parseInt(nilaiBaru);
    let item = keranjangBelanja[index];

    if (isNaN(qty) || qty < 1) {
        qty = 1;
    }

    // Validasi pencegahan melewati stok maksimal secara real-time di UI
    if (qty > item.stokMax) {
        alert("Stok tidak mencukupi! Stok maksimal tersedia: " + item.stokMax);
        qty = item.stokMax;
    }

    item.qty = qty;
    renderTabelKeranjang();
}

// Fungsi Pemicu saat Kasir mengubah nilai Diskon Persen di Tabel
function updateDiskonKeranjang(index, nilaiBaru) {
    let diskon = parseFloat(nilaiBaru);
    
    if (isNaN(diskon) || diskon < 0) diskon = 0;
    if (diskon > 100) diskon = 100; // Proteksi diskon tidak boleh lebih dari 100%

    keranjangBelanja[index].diskonPersen = diskon;
    renderTabelKeranjang();
}

// Fungsi untuk Menghapus salah satu Item dari Keranjang
function hapusItemKeranjang(index) {
    keranjangBelanja.splice(index, 1);
    renderTabelKeranjang();
}

// Fungsi pembantu untuk sinkronisasi nilai Grand Total ke papan informasi kasir lu
function updateGrandTotalUI(total) {
    // Sesuaikan id element papan total yang lu punya di Column-3 / Info Pembayaran
    const txtTotalPapan = document.getElementById("txtTotalBayarPapan"); 
    const inputTotalHidden = document.getElementById("totalBayarHidden"); // input form Penjualan.totalBayar

    if (txtTotalPapan) {
        txtTotalPapan.innerText = "Rp " + total.toLocaleString('id-ID');
    }
    if (inputTotalHidden) {
        inputTotalHidden.value = total;
    }
    
    // Jika ada fungsi hitung kembalian otomatis di JS lu, panggil juga di sini
    if (typeof hitungKembalian === "function") {
        hitungKembalian();
    }
}

// Bonus: Biar kasir bisa langsung pencet tombol 'Enter' setelah ngetik kode barang
document.getElementById("inputKodeBarang").addEventListener("keypress", function(e) {
    if (e.key === "Enter") {
        e.preventDefault(); // Cegah form ke-submit tidak sengaja
        tambahBarangByKode();
    }
});






//4.======================BAGIAN BOARD TOTAL BAYAR ====================
function updateGrandTotalUI(total) {
    // 1. Tembak papan skor utama kasir yang warna hitam (HTML baru lu)
    const displayTotalPapan = document.getElementById("displayTotalBayar"); 
    
    // 2. Tembak input hidden yang mengikat ke property objek Penjualan Java (jika ada di form)
    const inputTotalHidden = document.getElementById("totalBayarHidden"); 

    // Update teks di papan hitam dengan format mata uang Rupiah
    if (displayTotalPapan) {
        displayTotalPapan.innerText = total.toLocaleString('id-ID');
    }
    
    // Set nilai angka polos ke input hidden form biar kebaca sama Java @ModelAttribute
    if (inputTotalHidden) {
        inputTotalHidden.value = total;
    }
    
    // Jika nanti lu bikin fungsi hitung kembalian otomatis, panggil juga di sini
    if (typeof hitungKembalian === "function") {
        hitungKembalian();
    }
}