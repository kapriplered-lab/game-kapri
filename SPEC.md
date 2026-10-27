# LEVEL UP: REAL LIFE — Spesifikasi MVP 1.0

## Konsep
Aplikasi Android pribadi yang mengubah kebiasaan nyata menjadi progres RPG fantasi. Fokus versi pertama adalah loop utama: pilih quest → kerjakan di dunia nyata → klaim hadiah → naik level → buka skill.

## Prinsip produk
- Offline-first: tidak ada akun, backend, atau permission internet.
- Penyimpanan lokal dengan `SharedPreferences` untuk MVP.
- Reward tidak mengurangi XP atau memberi hukuman berat saat quest terlewat.
- Quest hanya dapat diklaim sekali per hari kalender lokal.
- Versi pertama mengutamakan loop gameplay yang stabil daripada fitur banyak.

## Fitur MVP
1. **Hero dashboard:** level, XP, progres ke level berikutnya, gold, skill points, dan ringkasan quest hari ini.
2. **Quest board:** empat quest awal, hadiah XP/gold, status selesai hari ini, membuat quest custom, dan menghapus quest custom.
3. **Level progression:** XP dari quest meningkatkan level; kebutuhan XP bertambah seiring level. Level baru memberi satu skill point.
4. **Skill tree awal:** tiga milestone — Warrior's Grit, Scholar's Focus, dan Guardian's Oath. Skill membutuhkan level 2 dan satu skill point.
5. **Local save:** level, XP, gold, skill points, daftar quest, tanggal klaim, dan status skill tersimpan saat ada perubahan.
6. **UI:** tema dark fantasy dengan panel hijau gelap dan aksen emas, tanpa library UI eksternal.

## Aturan progres
- XP untuk naik level: `100 + 25 × (level - 1)`.
- Quest bawaan: 15–30 XP dan 8–15 gold.
- Setiap level baru memberi 1 skill point.
- Quest ditandai selesai berdasarkan tanggal lokal `yyyy-MM-dd`.

## Model data lokal
- Player: `level`, `xp`, `coins`, `skillPoints`.
- Quest: `id`, `title`, `stat`, `xp`, `coins`, `custom`, `completedDate`.
- Skill: `name`, `branch`, `desc`, `requiredLevel`, `unlocked`.

## Batasan yang diketahui
- Belum ada backup/restore; hapus data aplikasi atau uninstall bisa menghapus progres.
- Skill tree masih berupa milestone/unlock, belum mengubah gameplay quest.
- Belum ada notifikasi, boss quest mingguan, kalender historis, sinkronisasi, atau statistik panjang.
- Belum ada automated UI tests atau hasil build APK terverifikasi pada perangkat di lingkungan ini.

## Tahap berikutnya
1. Build APK dan tes instalasi di perangkat Android.
2. Tes persistensi setelah force-close/restart, quest harian, XP overflow saat naik level, dan skill point.
3. Tambah ekspor/impor backup JSON.
4. Tambah pengingat lokal yang bisa diatur pengguna.
5. Perluas skill tree dan ringkasan progres mingguan.
