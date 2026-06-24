// =================================================================
// 👑 KODE UTUH: penjualan.js
// =================================================================

// ==================== 1. MODUL CUSTOMER DAN MEMBER ===================
const sectionNonMember = document.getElementById("sectionNonMember");
const sectionMember = document.getElementById("sectionMember");
const txtNamaCustomer = document.getElementById("txtNamaCustomer");
const txtNoTelepon = document.getElementById("txtNoTelepon");
const idCustomerInput = document.getElementById("idCustomerTerpilih");
const btnResetMember = document.getElementById("btnResetMember");

// ------ State Customer Aktif -----
let customerTerpilih = null;

// ------ CARI MEMBER - MODAL PENCARIAN --------
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
    // Kita pastikan kalau telepon itu null/undefined, kita ganti jadi strip
    const telpBersih = (telepon === null || telepon === undefined || telepon === "null") ? "-" : telepon;

    customerTerpilih = {
        idCustomer: id, 
        namaCustomer: nama, 
        noTelepon: telpBersih
    };
    
    updateMemberUI();
    document.getElementById("modalCariMember").style.display = "none";
}

// ------ TAMBAH MEMBER - MODAL FORM ------------
document.getElementById("btnTambahMember").addEventListener("click", function() {
    document.getElementById("modalTambahMember").style.display = "flex";
});

// Tutup Modal
document.getElementById("btnTutupModalTambah").addEventListener("click", function() {
    document.getElementById("modalTambahMember").style.display = "none";
});

// Submit form - tambah member baru via AJAX
document.getElementById("btnSimpanMemberBaru").addEventListener("click", function() {
    const nama = document.getElementById("inputNamaBaru").value.trim();
    const telepon = document.getElementById("inputTelpBaru").value.trim() || "-";

    if(!nama) {
        alert("Nama member wajib diisi!");
        return;
    }

    // Pengiriman ke backend
    fetch("/customer/simpan-ajax", {
        method: "POST",
        headers: { "Content-Type" : "application/json" },
        body: JSON.stringify({ 
            namaCustomer: nama, 
            noTelepon: telepon 
        })
    })
    .then(res => {
        if (!res.ok) {
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
        document.getElementById("modalTambahMember").style.display = "none";

        // Reset form inputan
        document.getElementById("inputNamaBaru").value = "";
        document.getElementById("inputTelpBaru").value = "";
    })
    .catch((err) => {
        console.error("Eror:", err);
        alert("Gagal menyimpan data! Pesan eror: " + err.message);
    });
});

// Event listener click baris tabel hasil cari member
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

// ------ RESET / HAPUS MEMBER MURNI ------
btnResetMember.addEventListener("click", function() {
    customerTerpilih = null;
    idCustomerInput.value = "";
    sectionMember.style.display    = "none";
    sectionNonMember.style.display = "block";
    btnResetMember.style.display   = "none";
});

// ------ UPDATE TAMPILAN UI MEMBER AKTIF ------
function updateMemberUI() {
    if (customerTerpilih) {
        txtNamaCustomer.innerText = customerTerpilih.namaCustomer;
        txtNoTelepon.innerText    = customerTerpilih.noTelepon; 
        idCustomerInput.value     = customerTerpilih.idCustomer;

        sectionNonMember.style.display = "none";
        sectionMember.style.display    = "block";
        btnResetMember.style.display   = "inline-block";
    }
}

// ------ FILTER MEMBER SINKRONISASI LOKAL ------
function filterMemberLokal() {
    var input = document.getElementById("inputanCariMember");
    var filter = input.value.toLowerCase();
    var rows = document.querySelectorAll("#tabelHasilCari tr");
    
    for (var i = 0; i < rows.length; i++) {
        var namaCustomer = rows[i].getAttribute("data-nama") || rows[i].getAttribute("th:data-nama") || "";
        if (namaCustomer.toLowerCase().indexOf(filter) > -1) {
            rows[i].style.display = ""; 
        } else {
            rows[i].style.display = "none"; 
        }
    }
}


// ==================== 2. MODUL SISTEM KERANJANG BELANJA ===================
let keranjangBelanja = [];
let grandTotalGlobal = 0; // Mengunci angka total murni untuk validasi uang kasir

function tambahBarangByKode() {
    const inputKode = document.getElementById("inputKodeBarang");
    const kodeCari = inputKode.value.trim().toLowerCase();
    if (kodeCari === "") { alert("Ketik kode barang terlebih dahulu!"); return; }

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

    if (!barangDitemukan) {
        alert("Barang dengan kode '" + inputKode.value + "' tidak ditemukan!");
        inputKode.value = "";
        return;
    }

    const itemEksis = keranjangBelanja.find(item => item.idBarang === barangDitemukan.idBarang);
    if (itemEksis) {
        if (itemEksis.qty + 1 > barangDitemukan.stok) { alert("Stok tidak mencukupi!"); return; }
        itemEksis.qty += 1;
    } else {
        if (barangDitemukan.stok < 1) { alert("Stok barang ini kosong!"); return; }
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
    inputKode.value = "";
    renderTabelKeranjang();
}

function renderTabelKeranjang() {
    const tbody = document.getElementById("tabelKeranjang");
    tbody.innerHTML = "";

    if (keranjangBelanja.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: #999; padding: 20px;">Keranjang masih kosong. Ketik Kode Barang!</td></tr>`;
        updateGrandTotalUI(0);
        return;
    }

    let grandTotal = 0;
    keranjangBelanja.forEach((item, index) => {
        let subtotal = (item.hargaJual * item.qty) - ((item.hargaJual * item.qty) * (item.diskonPersen / 100));
        grandTotal += subtotal;

        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td><b>${item.namaBarang}</b> <br><small style="color:#666">${item.kodeBarang}</small></td>
            <td>Rp ${item.hargaJual.toLocaleString('id-ID')}</td>
            <td><input type="number" value="${item.qty}" min="1" max="${item.stokMax}" style="width: 60px; text-align: center;" onchange="updateQtyKeranjang(${index}, this.value)"></td>
            <td>${item.brand}</td>
            <td><input type="number" value="${item.diskonPersen}" min="0" max="100" style="width: 50px; text-align: center;" onchange="updateDiskonKeranjang(${index}, this.value)"> %</td>
            <td><b>Rp ${subtotal.toLocaleString('id-ID')}</b></td>
            <td><button type="button" onclick="hapusItemKeranjang(${index})" style="background: #ef4444; color: white; border: none; padding: 4px 8px; border-radius: 4px; cursor: pointer;">Hapus</button></td>
        `;
        tbody.appendChild(tr);
    });

    updateGrandTotalUI(grandTotal);
}

function updateQtyKeranjang(index, nilaiBaru) {
    let qty = parseInt(nilaiBaru);
    let item = keranjangBelanja[index];
    if (isNaN(qty) || qty < 1) qty = 1;
    if (qty > item.stokMax) { alert("Stok maksimal: " + item.stokMax); qty = item.stokMax; }
    item.qty = qty;
    renderTabelKeranjang();
}

function updateDiskonKeranjang(index, nilaiBaru) {
    let diskon = parseFloat(nilaiBaru);
    if (isNaN(diskon) || diskon < 0) diskon = 0;
    if (diskon > 100) diskon = 100;
    keranjangBelanja[index].diskonPersen = diskon;
    renderTabelKeranjang();
}

function hapusItemKeranjang(index) { keranjangBelanja.splice(index, 1); renderTabelKeranjang(); }

document.getElementById("inputKodeBarang").addEventListener("keypress", function(e) {
    if (e.key === "Enter") { e.preventDefault(); tambahBarangByKode(); }
});


// ==================== 3. MODUL VALIDASI & LOMPAT HALAMAN ===================
const formTransaksi = document.getElementById("formTransaksi");

if (formTransaksi) {
    formTransaksi.addEventListener("submit", function(e) {
        // 1. Validasi keranjang kosong
        if (keranjangBelanja.length === 0) {
            e.preventDefault(); 
            alert("Transaksi Ditolak! Keranjang belanja Anda masih kosong.");
            return;
        }

        // 2. Validasi nominal uang tunai jika memilih metode TUNAI
        const metode = document.getElementById("selectMetodePembayaran").value;
        const tunai = parseFloat(document.getElementById("jumlahBayar").value) || 0;

        if (metode === 'TUNAI' && tunai < grandTotalGlobal) {
            e.preventDefault();
            alert("Transaksi Ditolak! Uang tunai yang diterima kurang dari total nominal belanja.");
            return;
        }

        // 3. Amankan rangkuman list data barang belanjaan ke Storage Lokal sebelum lompat halaman
        sessionStorage.setItem("keranjangBelanja", JSON.stringify(keranjangBelanja));
    });
}


// ==================== 4. BAGIAN BOARD TOTAL BAYAR KASIR ===================
function updateGrandTotalUI(total) {
    grandTotalGlobal = total;
    const displayTotalPapan = document.getElementById("displayTotalBayar"); 
    const inputTotalHidden = document.getElementById("totalBayarHidden"); 
    if (displayTotalPapan) displayTotalPapan.innerText = total.toLocaleString('id-ID');
    if (inputTotalHidden) inputTotalHidden.value = total;
}

// ==================== 5. MEMUAT FRAGMENT MODAL PEMBAYARAN DENGAN AJAX ===================
function bukaModalPembayaranFragment() {
    // 1. Validasi keranjang kosong
    if (keranjangBelanja.length === 0) {
        alert("Transaksi Ditolak! Keranjang belanja Anda masih kosong.");
        return;
    }

    // 2. Validasi nominal uang tunai jika memilih metode TUNAI
    const metode = document.getElementById("selectMetodePembayaran").value;
    const tunai = parseFloat(document.getElementById("jumlahBayar").value) || 0;

    if (metode === 'TUNAI' && tunai < grandTotalGlobal) {
        alert("Transaksi Ditolak! Uang tunai yang diterima kurang dari total nominal belanja.");
        return;
    }

    // 3. Amankan rangkuman list data barang belanjaan ke Storage Lokal sebelum memuat fragment
    sessionStorage.setItem("keranjangBelanja", JSON.stringify(keranjangBelanja));

    // 4. Load fragment modal dari server
    const customerId = document.getElementById("idCustomerTerpilih").value || '';
    const idAkun = document.getElementById("idAkunKasir").value || '1';

    const url = `/penjualan/pembayaran?metode=${metode}&customerId=${customerId}&totalBayar=${grandTotalGlobal}&tunaiDiterima=${tunai}&idAkun=${idAkun}`;

    fetch(url)
        .then(res => {
            if (!res.ok) {
                throw new Error("Gagal mengambil template pembayaran.");
            }
            return res.text();
        })
        .then(html => {
            const container = document.getElementById("tempatModalPembayaranTimbul");
            container.innerHTML = html;
            
            // Eksekusi script di dalam HTML yang di-inject dinamis
            const scripts = container.querySelectorAll("script");
            scripts.forEach(oldScript => {
                const newScript = document.createElement("script");
                newScript.text = oldScript.text;
                document.body.appendChild(newScript);
            });
        })
        .catch(err => {
            console.error("Error:", err);
            alert("Gagal memuat konfirmasi pembayaran!");
        });
}

// ==================== 6. PENGELOLAAN INPUT KASIR NON-TUNAI (LOCK FIELD) ===================
function handleMetodePembayaranChange() {
    const selectMetode = document.getElementById("selectMetodePembayaran");
    const inputJumlahBayar = document.getElementById("jumlahBayar");

    if (selectMetode && inputJumlahBayar) {
        const updateInputState = () => {
            if (selectMetode.value === "TUNAI") {
                inputJumlahBayar.disabled = false;
                inputJumlahBayar.placeholder = "Kosongkan jika non-tunai";
                inputJumlahBayar.style.backgroundColor = "";
            } else {
                inputJumlahBayar.disabled = true;
                inputJumlahBayar.value = "";
                inputJumlahBayar.placeholder = "Terkunci (Pembayaran Non-Tunai)";
                inputJumlahBayar.style.backgroundColor = "#e2e8f0"; // Warna abu-abu terkunci
            }
        };

        selectMetode.addEventListener("change", updateInputState);
        updateInputState(); // Jalankan sekali saat load halaman awal
    }
}

if (document.readyState !== 'loading') {
    handleMetodePembayaranChange();
} else {
    document.addEventListener("DOMContentLoaded", handleMetodePembayaranChange);
}