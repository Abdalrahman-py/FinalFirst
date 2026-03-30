# FinalFirst — Exam Project

## Project Identity
- **Package:** `com.finalfirst`
- **Language:** Kotlin (no Java)
- **minSdk:** 24 | **targetSdk:** 36
- **Build system:** Gradle with version catalog (`gradle/libs.versions.toml`)

## Spec Summary
| Feature | Detail |
|---|---|
| Read contacts | `ContentResolver` + `ContactsContract.CommonDataKinds.Phone` (name + phone) |
| Local storage | Room Database |
| Display | `RecyclerView` in Tab 1 |
| Add contact | Form in Tab 2 |
| Navigation | `TabLayout` + `ViewPager2` |
| Notification | `NotificationChannel` → message: `"تم اضافة رقم جديد"` on successful insert |
| Permission | `READ_CONTACTS` runtime permission |
| Bonus | LiveData for auto UI refresh + prevent duplicate phone numbers |

## Architecture — MVVM
```
UI (Activity / Fragments)
  └── ViewModel  (ContactViewModel)
        └── Repository  (ContactRepository)
              ├── Room DAO  (ContactDao)
              └── ContentResolver  (ContactsImporter)
```

## File Structure
```
app/src/main/java/com/finalfirst/
├── data/
│   ├── local/
│   │   ├── Contact.kt           # Room @Entity
│   │   ├── ContactDao.kt        # DAO: insertContact(), getAllContacts()
│   │   └── AppDatabase.kt       # RoomDatabase singleton
│   └── repository/
│       └── ContactRepository.kt
├── contacts/
│   └── ContactsImporter.kt      # ContentResolver logic
├── notification/
│   └── NotificationHelper.kt    # Channel creation + send
├── ui/
│   ├── MainActivity.kt          # TabLayout + ViewPager2
│   ├── adapter/
│   │   ├── ContactsAdapter.kt   # RecyclerView adapter
│   │   └── ViewPagerAdapter.kt  # FragmentStateAdapter
│   └── fragments/
│       ├── ContactsFragment.kt  # Tab 1 — RecyclerView
│       └── AddContactFragment.kt# Tab 2 — add form
└── viewmodel/
    └── ContactViewModel.kt
```

## Room Entity
```kotlin
@Entity(
    tableName = "contacts",
    indices = [Index(value = ["phone"], unique = true)]  // bonus: no duplicates
)
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String
)
```

## DAO
```kotlin
@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContact(contact: Contact): Long   // returns -1 if duplicate

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): LiveData<List<Contact>>        // bonus: LiveData
}
```

## Dependencies to Add
Add to `gradle/libs.versions.toml` and `app/build.gradle.kts`:

**Versions:**
- `room = "2.7.0"`
- `lifecycle = "2.9.0"`
- `coroutines = "1.10.1"`
- `viewpager2 = "1.1.0"`
- `kotlin = "2.1.20"` (also add kotlin plugin)

**Libraries:**
- `room-runtime`, `room-ktx`, `room-compiler` (kapt)
- `lifecycle-viewmodel-ktx`, `lifecycle-livedata-ktx`
- `kotlinx-coroutines-android`
- `viewpager2`

## Coding Rules
- **No Jetpack Compose** — XML layouts only
- Room DB calls must use `suspend` functions or return `LiveData`; call from `viewModelScope`
- `READ_CONTACTS` permission must be checked/requested before `ContentResolver` query
- `NotificationChannel` must be created at app start (do it in `MainActivity.onCreate`)
- Notification channel ID: `"contacts_channel"`
- Use `OnConflictStrategy.IGNORE` + check return value to detect/handle duplicates
- `ViewPager2` + `FragmentStateAdapter` for tabs (not deprecated `ViewPager`)
- `AppDatabase` must be a singleton (use `companion object` with `@Volatile`)

## Manifest Permissions
```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## Do Not
- Do not use Java in any file
- Do not use deprecated `ViewPager` — use `ViewPager2`
- Do not run Room operations on the main thread
- Do not add features beyond the spec