

# 🛍️ FiyatRadar – Barkod Taramalı Fiyat Karşılaştırma Uygulaması

FiyatRadar, kullanıcıların barkodları tarayarak farklı mağazalarda ürün fiyatlarını karşılaştırmalarını sağlayan bir mobil uygulamadır.
Bu proje, React Native ile frontend, Spring Boot ile backend, ve PostgreSQL gibi bir veritabanı kullanılarak geliştirilmiştir.

# 🚀 Proje Özeti

FiyatRadar, kullanıcıların barkodları tarayarak ürün fiyatlarını kolayca karşılaştırmalarını sağlar. Uygulama, farklı mağazalardan alınan fiyat bilgilerini karşılaştırarak en uygun fiyatı bulmalarını amaçlar. Ayrıca, kullanıcılar fiyatları ve barkodları güncelleyebilir ve geçerli barkodları kontrol edebilir.

# 🛠️ Kullanılan Teknolojiler

Frontend: React Native

Backend: Spring Boot

Veritabanı: PostgreSQL

Barkod Okuma: Google ML Kit veya ZXing

Authentication: JWT (şu an için entegrasyon yapılmamıştır)

📋 Kurulum

# 1. Backend Kurulumu

Backend kısmı Spring Boot ile geliştirilmiştir. Projeyi çalıştırmak için:

1.JDK 23 veya üzeri bir sürüm yüklü olmalıdır.

2.Proje dizininde aşağıdaki komutu kullanarak bağımlılıkları yükleyebilirsiniz:

./mvnw clean install

3.Ardından Spring Boot uygulamasını başlatmak için şu komutu kullanın:

./mvnw spring-boot:run

# 2. Frontend Kurulumu
Frontend kısmı React Native ile geliştirilmiştir. Uygulamayı çalıştırmak için:

1.Node.js ve npm/yarn kurulu olmalıdır.

2.Proje dizininde gerekli bağımlılıkları yüklemek için şu komutu kullanın:

npm install
veya
yarn install

3.Uygulamayı başlatmak için:

npm start
veya
yarn start

# 3. Veritabanı Yapılandırması
Proje PostgreSQL veritabanı kullanmaktadır. Veritabanı bağlantılarını yapılandırmak için application.properties dosyasını düzenleyebilirsiniz.

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/fiyatradar

spring.datasource.username=your-username

spring.datasource.password=your-password

spring.jpa.hibernate.ddl-auto=update

# 4. API Endpoints
Aşağıda, projenin önemli API endpoint'leri listelenmiştir:
<img width="633" alt="Ekran Resmi 2025-03-27 12 00 52" src="https://github.com/user-attachments/assets/496dfb6f-754e-4ce5-baee-248e1a58edd6" />

# 5. Testler
Projede temel testler yazılmıştır, ancak şu an için bazı testler devre dışı bırakılmıştır. Testlerin doğru çalıştığından emin olmak için:

./mvnw test
komutunu çalıştırabilirsiniz.

# 🤝 Katkı

Bu projeye katkıda bulunmak için:

Repo'yu fork edin.

Yeni bir branch oluşturun (git checkout -b feature/your-feature).

Yapacağınız değişiklikleri ekleyin ve commit yapın (git commit -am 'Add new feature').

Değişikliklerinizi push edin (git push origin feature/your-feature).

Pull request oluşturun.

# 📄 Lisans

Bu proje MIT Lisansı ile lisanslanmıştır.
