# Java-to-Kotlin
Description : change java code to kotlin [ notepad app ] (Udacity)

### Anko Library
```kotlin
    fun insert(vararg notes: Note) {
        val values = fromNotes(notes)
        val db = helper.writableDatabase
        // anko library 사용해서 코드를 clean 하게 만들 수 있다
        db.transaction {
            for (value in values) {
                insert(_TABLE_NAME, null, value)
            }
        }
    }
    
    // anko 내부 코드 (기존 코드)
    fun SQLiteDatabase.transaction(code: SQLiteDatabase.() -> Unit) {
        try {
            beginTransaction()
            code()
            setTransactionSuccessful()
        } catch (e: TransactionAbortException) {
            // Do nothing, just stop the transaction
        } finally {
            endTransaction()
        }
    }
```

### Kotlin
```kotlin
    // JvmStatic Annotation
    // 공식 문서에 좋은 예제가 있었다.
    class C {
        companion object {
            @JvmStatic fun foo() {}
            fun bar() {}
        }
    }

    C.foo(); // works fine
    C.bar(); // error: not a static method
    C.Companion.foo(); // instance method remains
    C.Companion.bar(); // the only way it works

    // same for named objects :
    object Obj {
        @JvmStatic fun foo() {}
        fun bar() {}
    }
    
    @JvmStatic
    lateinit var notes: NoteDatabase 
      private set
    // lateinit
    // kotlin 에서는 non-null type 은 무조건 초기화하도록 되어 있다.
    // 하지만 이는 불편할 수 있다.
    // 이 경우 lateinit 을 이용해서 나중에 초기화하겠다고 컴파일러에게 약속을 해줄 수 있다.
        

    // vararg : variable number of arguments
    // ids = arrayOf(1,2,3,4)
    /*
    fun <T> asList(vararg ts: T): List<T> {
        val result = ArrayList<T>()
        for (t in ts) // ts is an Array
            result.add(t)
        return result
    }
    */
    fun loadAllByIds(vararg ids: Int): List<Note> {
        val questionMarks = ids.map { "?" }.joinToString { ", " }
        val args = ids.map { it.toString() }
        val selection = "$_ID IN ($questionMarks)"
        val cursor = helper.readableDatabase.query(_TABLE_NAME,
                null,
                selection,
                args.toTypedArray(),
                null,
                null,
                CREATED_AT)
                
        // kotlin.io 자동으로 close() 호출
        return cursor.use(this::allFromCursor) // lambda expression 사용
    }
    
    
    private fun fromCursor(cursor: Cursor): Note {
        var col = 0
        
        // 기존 코드
        // var note = Note()
        // note.id = cursor.getInt(col++)
        // ... 생략
        // return note
        
        return Note().apply { // kotlin standard function
            id = cursor.getInt(col++)
            text = cursor.getString(col++)
            isPinned = cursor.getInt(col++) != 0
            createdAt = Date(cursor.getLong(col++))
            updatedAt = Date(cursor.getLong(col))
        }
    }
    
    
    // [ Generic ]
    // Java 의 wildcard 기능 == kotlin 의 in/out
    // ? extends T == out ; read only
    // ? super T == in ; write only
    // 감사하게도 잘 정리된 포스트를 참고할 수 있었다.
    private fun fromNotes(notes: Array<out Note>): List<ContentValues> {
        val values = ArrayList<ContentValues>()
        for (note in notes) {
            values.add(fromNote(note))
        }
        return values
    }
```
[Kotlin Generics 정의 - thdev](http://thdev.tech/kotlin/androiddev/2017/10/03/Kotlin-Generics.html)


### Kotlin Extension
```kotlin
    // view binding
    // Extension 에 관한 좋은 글 아래 URL
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //
        setSupportActionBar(toolbar)

        fab.setOnClickListener { startActivity(CreateActivity.get(this@MainActivity)) }

        recycler!!.layoutManager = LinearLayoutManager(this)
        recycler!!.addItemDecoration(SpaceItemDecoration(this, R.dimen.margin_small))
        recycler!!.adapter = NotesAdapter(this)
    }
```
[Kotlin의 Extension은 어떻게 동작하는가 part 1 - ChangW.Doh](https://medium.com/til-kotlin-ko/kotlin%EC%9D%98-extension%EC%9D%80-%EC%96%B4%EB%96%BB%EA%B2%8C-%EB%8F%99%EC%9E%91%ED%95%98%EB%8A%94%EA%B0%80-part-1-7badafa7524a)
