# Depo Yönetim Sistemi

Bu proje, bir deponun ürün kabul, stok takibi, sevkiyat ve raporlama süreçlerini yazılım ortamında modellemek amacıyla geliştirilmiş **GUI tabanlı (Java Swing)** bir Depo Yönetim Sistemi uygulamasıdır.

Proje, nesne tabanlı analiz ve tasarım prensiplerine uygun olarak geliştirilmiş; UML diyagramları ile desteklenmiştir.

---

## Projenin Amacı

- Depo yönetiminde ürün ve stok takibini sistematik hale getirmek  
- Stok giriş–çıkış işlemlerini kontrollü ve izlenebilir biçimde gerçekleştirmek  
- Kritik stok seviyelerini otomatik olarak tespit edebilmek  
- Nesne tabanlı programlama (OOP) ve UML tasarım yaklaşımlarını uygulamalı olarak göstermek  

---

## Sistem Özellikleri

### Ürün Yönetimi
- Ürün ekleme, güncelleme ve silme  
- Ürün kodu bazlı benzersizlik kontrolü  
- Kategori, birim, kritik seviye ve raf/konum bilgileri  

### Stok Yönetimi
- Stok girişi ve stok çıkışı işlemleri  
- Yetersiz stok durumlarında hata yönetimi  
- Güncel stok miktarının otomatik hesaplanması  

### Raporlama
- Kritik stok seviyesindeki ürünlerin görüntülenmesi  
- Stok giriş–çıkış hareket geçmişinin listelenmesi  

### Tedarikçi ve Sevkiyat
- Tedarikçi ekleme ve listeleme  
- Sevkiyat kaydı oluşturma (INBOUND / OUTBOUND)  
- Sevkiyat tipine göre stok giriş veya çıkışının otomatik yapılması  

### Hata Yönetimi
- Hatalı girişler için özel exception sınıfları  
- Kullanıcıya anlamlı hata ve bilgilendirme mesajları  

---

## Kullanılan Teknolojiler

- Programlama Dili: **Java 17**
- IDE: **Eclipse**
- GUI: **Java Swing**
- Mimari: **Katmanlı yapı (UI – Service – Model – Exception)**
- Veri Saklama: **Bellek içi (In-Memory)**

---

## Mimari ve Tasarım

Proje aşağıdaki UML diyagramları ile tasarlanmıştır:

- Use-Case Diyagramı  
- Sequence Diyagramları (Ürün Ekle, Stok Çıkışı)  
- Sınıf Diyagramı (Class Diagram)

Tasarımda kapsülleme, tek sorumluluk, kalıtım ve modülerlik prensipleri dikkate alınmıştır.

---

## Proje Yapısı

