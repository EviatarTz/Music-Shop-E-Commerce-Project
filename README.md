# Music Shop 🎵

פרויקט אקדמי של חנות כלי נגינה מקוונת (e-commerce), שנכתב ב-Java עם JSF ו-PrimeFaces, ומחובר למסד נתונים PostgreSQL.

## תוכן עניינים

- [טכנולוגיות](#טכנולוגיות)
- [דרישות מקדימות](#דרישות-מקדימות)
- [הקמת מסד הנתונים](#הקמת-מסד-הנתונים)
- [בניית הפרויקט](#בניית-הפרויקט)
- [פריסה (Deployment)](#פריסה-deployment)
- [תכונות עיקריות](#תכונות-עיקריות)
- [מבנה הפרויקט](#מבנה-הפרויקט)

## טכנולוגיות

- **Java** (ראה `pom.xml` לגרסת ה-compiler)
- **Jakarta Faces (Mojarra) 4.1.12** — JSF
- **PrimeFaces 15.0.17** — ספריית קומפוננטות UI
- **Weld 6.0.4** — CDI
- **Apache Tomcat 11.0.23** — שרת האפליקציה
- **PostgreSQL** — מסד הנתונים
- **Maven** — ניהול build ו-dependencies

## דרישות מקדימות

לפני שמריצים את הפרויקט, יש להתקין:

1. **JDK** בגרסה המתאימה (ראה `<maven.compiler.source>` ו-`<maven.compiler.target>` ב-`pom.xml`).
2. **Apache Maven**.
3. **Apache Tomcat 11.0.23** (או גרסה תואמת ל-Jakarta Servlet 6.1).
4. **PostgreSQL** מותקן ורץ על `localhost:5432`.

## הקמת מסד הנתונים

פרטי ההתחברות למסד הנתונים מוגדרים בקובץ `ConnectionManager.java`:

```
URL:      jdbc:postgresql://localhost:5432/music_shop_db
USER:     postgres
PASSWORD: 1234
```

### יצירת מסד הנתונים משחזור

הדאמפ של מסד הנתונים נמצא בתיקייה `database/music_shop_db.sql`.

```bash
createdb -U postgres music_shop_db
psql -U postgres -h localhost music_shop_db < database/music_shop_db.sql
```

> אם שם המשתמש/הסיסמה של PostgreSQL במחשב שלך שונים מ-`postgres`/`1234`, יש לעדכן את הערכים האלה גם בקובץ `ConnectionManager.java` וגם בפקודות לעיל.

## בניית הפרויקט

מתוך שורש הפרויקט:

```bash
mvn clean package
```

הפקודה תיצור קובץ WAR בנתיב:

```
target/music-shop-1.0-SNAPSHOT.war
```

## פריסה (Deployment)

1. יש להעתיק את קובץ ה-WAR שנוצר לתיקיית `webapps` של Tomcat:
   ```bash
   cp target/music-shop-1.0-SNAPSHOT.war <TOMCAT_HOME>/webapps/
   ```
2. להריץ את Tomcat (או להפעיל אותו מחדש אם הוא כבר רץ) — Tomcat יחלץ את ה-WAR אוטומטית.
3. לגשת לאפליקציה בכתובת:
   ```
   http://localhost:8080/music-shop-1.0-SNAPSHOT/
   ```

## תכונות עיקריות

- עיון במוצרים לפי קטגוריות ותת-קטגוריות.
- חיפוש מוצרים.
- עמוד מוצר מפורט עם הוספה לעגלת קניות (מחייב התחברות).
- עגלת קניות ותהליך checkout, כולל בדיקה והפחתה של מלאי (inventory) בפועל.
- הרשמה והתחברות משתמשים (לפי שם משתמש וסיסמה).
- היסטוריית הזמנות למשתמש מחובר.
- פאנל ניהול (admin).
- שכבת נגישות (accessibility) מובנית בכל האתר: הגדלה/הקטנה של טקסט, ניגודיות גבוהה, גווני אפור, הדגשת קישורים, גופן קריא, עצירת אנימציות, ואיפוס הגדרות.

## מבנה הפרויקט

```
src/main/java/...        קוד Java: managed beans, DAO, services
src/main/webapp/...      דפי JSF (.xhtml), CSS, JS
pom.xml                  הגדרות Maven
```

## הערות

- קובץ ה-WAR עצמו **לא** נשמר ב-repository (ראה `.gitignore`) — הוא נוצר מחדש בכל `mvn clean package`.
- פרטי ההתחברות למסד הנתונים כרגע חשופים בקוד (`ConnectionManager.java`). לפרויקט שמפורסם ב-repository ציבורי, מומלץ בעתיד להעביר אותם לקובץ `.properties` חיצוני שלא עולה ל-git.
