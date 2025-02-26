# InstaClone Uygulaması

Bu proje, Firebase tabanlı bir sosyal medya uygulaması olan **InstaClone**'un temel işlevlerini içermektedir. Kullanıcılar giriş yapabilir, fotoğraf yükleyebilir ve gönderileri görüntüleyebilir.

## Özellikler
- **Kullanıcı Kimlik Doğrulama:** Firebase Authentication kullanılarak oturum açma ve kayıt işlemleri sağlanmaktadır.
- **Gönderi Paylaşma:** Kullanıcılar resim yükleyebilir ve yorum ekleyerek paylaşım yapabilir.
- **Gönderileri Görüntüleme:** Kullanıcılar, paylaşılan gönderileri akış (feed) ekranında görebilir.
- **Oturum Yönetimi:** Kullanıcılar çıkış yapabilir ve tekrar giriş yapabilir.

## Kullanılan Teknolojiler
- **Kotlin:** Android uygulama geliştirme dili olarak kullanılmıştır.
- **Firebase Authentication:** Kullanıcı kimlik doğrulaması için kullanılmıştır.
- **Firebase Firestore:** Gönderi verilerinin depolanmasını sağlar.
- **Firebase Storage:** Kullanıcıların yüklediği görsellerin depolanmasını sağlar.
- **Picasso:** Görselleri yüklemek ve göstermek için kullanılmıştır.
- **RecyclerView:** Gönderilerin listelenmesi için kullanılmıştır.

## Proje Yapısı

### Ana Bileşenler
- **MainActivity.kt:** Kullanıcı giriş ve kayıt işlemlerini yönetir.
- **FeedActivitiy.kt:** Kullanıcıların gönderileri görüntüleyebildiği ekranı yönetir.
- **UploadActivitiy.kt:** Kullanıcıların gönderi yükleme işlemlerini yönetir.
- **Post.kt:** Kullanıcı gönderi modelini temsil eden veri sınıfıdır.
- **FeedRecyclerAdapter.kt:** Gönderileri listelemek için RecyclerView adaptörü.

### UI Bileşenleri
- **RecyclerView (Feed Listesi):** Kullanıcıların gönderileri görüntülediği bileşen.
- **EditText ve Button:** Kullanıcı giriş ve kayıt işlemleri için kullanılmaktadır.
- **ImageView:** Kullanıcıların yüklediği görselleri göstermek için kullanılır.

## Kullanım
1. **Kayıt Olun veya Giriş Yapın.**
2. **Ana Akışta (Feed) Diğer Gönderileri Görüntüleyin.**
3. **Fotoğraf ve Yorum ile Yeni Gönderi Paylaşın.**
4. **Çıkış Yapmak İçin Menüden Sign Out Seçeneğini Kullanın.**

## Kurulum
1. **Projeyi klonlayın:**
   ```sh
   git clone https://github.com/kullaniciadi/InstaClone.git
   ```
2. **Android Studio ile açın.**
3. **Firebase yapılandırmasını tamamlayın (google-services.json ekleyin).**
4. **Cihaz veya emülatör seçerek çalıştırın.**

## Geliştirme Notları
- **Firebase Firestore ile veritabanı entegrasyonu sağlandı.**
- **Picasso kullanılarak gönderi görsellerinin yüklenmesi sağlandı.**
- **Firebase Authentication ile kullanıcı kimlik doğrulaması yapıldı.**

## Katkıda Bulunma
Projeye katkıda bulunmak için bir pull request oluşturabilir veya issue açabilirsiniz.

## Lisans
Bu proje MIT Lisansı ile lisanslanmıştır. Daha fazla bilgi için LICENSE dosyasına bakınız.

