package managedbeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.model.SelectItem;
import jakarta.faces.model.SelectItemGroup;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.*;

@Named("categoryBean")
@ApplicationScoped
public class CategoryBean implements Serializable {

    public static class Subcategory implements Serializable {
        private final int id;
        private final String name;
        private final String folder;
        private final int minId;
        private final int maxId;

        public Subcategory(int id, String name, String folder, int minId, int maxId) {
            this.id = id;
            this.name = name;
            this.folder = folder;
            this.minId = minId;
            this.maxId = maxId;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getFolder() { return folder; }
        public int getMinId() { return minId; }
        public int getMaxId() { return maxId; }
    }

    // מבנה עזר פנימי בזמן בנייה בלבד - עוד לפני שיש מזהה יציב
    private static class RawSub {
        final String name, folder;
        final int minId, maxId;
        RawSub(String name, String folder, int minId, int maxId) {
            this.name = name;
            this.folder = folder;
            this.minId = minId;
            this.maxId = maxId;
        }
    }

    private final Map<String, String> categoryNames = new LinkedHashMap<>();
    private final Map<String, String> categoryFolders = new LinkedHashMap<>();
    private final Map<String, List<Subcategory>> subcategoriesByCategory = new LinkedHashMap<>();

    // מזהה תת-קטגוריה (1-46) -> נתיב תיקייה יחסי, לדוגמה "Keyboards/Synths"
    private final Map<Integer, String> imageFolderById = new LinkedHashMap<>();
    // מזהה תת-קטגוריה -> שם תצוגה בעברית, לנוחות (למשל בטבלת הניהול)
    private final Map<Integer, String> subcategoryNameById = new LinkedHashMap<>();

    private final List<SelectItem> groupedSubcategoryItems = new ArrayList<>();

    private int nextId = 1;

    @PostConstruct
    public void init() {

        categoryNames.put("keyboards", "קלידים");
        categoryNames.put("guitars", "גיטרות");
        categoryNames.put("drums", "תופים וכלי הקשה");
        categoryNames.put("wind", "כלי נשיפה");
        categoryNames.put("strings", "כלי קשת");
        categoryNames.put("studio", "ציוד לאולפן");
        categoryNames.put("stage", "ציוד במה");

        categoryFolders.put("keyboards", "Keyboards");
        categoryFolders.put("guitars", "Guitars");
        categoryFolders.put("drums", "Drums");
        categoryFolders.put("wind", "Wind Instruments");
        categoryFolders.put("strings", "String Instruments");
        categoryFolders.put("studio", "Studio Equipment");
        categoryFolders.put("stage", "Stage Equipment");

        addCategory("keyboards",
                raw("פסנתרי כנף", "Grand pianos", 228, 264),
                raw("פסנתרים עומדים", "Acoustic pianos", 265, 288),
                raw("פסנתרים חשמליים נייחים", "Digital piano", 289, 343),
                raw("פסנתרים חשמליים ניידים", "Protable electric piano", 344, 374),
                raw("סינתיסייזרים", "Synths", 375, 418),
                raw("ציוד היקפי לקלידים", "Keyboard equipment", 419, 450)
        );

        addCategory("guitars",
                raw("גיטרות קלאסיות", "Classical Guitars", 451, 484),
                raw("גיטרות אקוסטיות", "Acoustic Guitars", 485, 529),
                raw("גיטרות חשמליות", "Electric Guitars", 530, 649),
                raw("גיטרות בס", "Bass Guitars", 650, 704),
                raw("כלי מיתר אחרים", "Other Instruments", 705, 751),
                raw("פדאלים", "Pedals", 752, 846),
                raw("מגברים", "Amps", 847, 870),
                raw("ציוד היקפי לכלי מיתר", "Guitar Equipment", 871, 1133)
        );

        addCategory("drums",
                raw("מערכות תופים אקוסטיים", "Acoustic Drumset", 1134, 1149),
                raw("מערכות תופים חשמליים", "Electric Drumset", 1150, 1171),
                raw("תופים בודדים", "Drum Parts", 1172, 1221),
                raw("מצילות", "Cymbals", 1222, 1268),
                raw("כלי הקשה נוספים", "Precussions", 1269, 1308),
                raw("ציוד היקפי לתופים", "Drum and Precussions equipment", 1309, 1428)
        );

        addCategory("wind",
                raw("חצוצרה", "Trumpets", 1429, 1447),
                raw("טרומבון", "Trombones", 1448, 1458),
                raw("סקסופון", "Saxophones", 1459, 1479),
                raw("חליל צד", "Flutes", 1480, 1497),
                raw("טובה", "Tubes", 1498, 1501),
                raw("קלרינט", "Clarinets", 1502, 1523),
                raw("מפוחית", "Harmonicas", 1524, 1548),
                raw("ציוד היקפי לכלי נשיפה", "Wind Instruments equipment", 1549, 1585)
        );

        addCategory("strings",
                raw("כינור", "Violins", 1586, 1615),
                raw("צ'לו", "Cellos", 1616, 1633),
                raw("קונטרבס", "CounterBases", 1634, 1650),
                raw("ציוד היקפי לכלי קשת", "String Instruments Equipment", 1651, 1666)
        );

        addCategory("studio",
                raw("מיקרופון", "Microphones", 1667, 1688),
                raw("מיקסר", "Mixers", 1689, 1706),
                raw("אוזניות אולפן", "Studio Earphones", 1707, 1724),
                raw("ממיר אותות", "Converters", 1725, 1732),
                raw("כרטיס קול", "Sound Cards", 1733, 1747),
                raw("מגבר אוזניות", "Earphones Amps", 1748, 1754),
                raw("אקוסטיקה לאולפן", "Studio Acoustics", 1755, 1764)
        );

        addCategory("stage",
                raw("רמקולים", "Speakers", 1765, 1779),
                raw("מיקסר", "Mixers", 1780, 1798),
                raw("מגברי הספק", "Power Amps", 1799, 1823),
                raw("ממיר אותות", "Converters", 1824, 1832),
                raw("מערכות מיקרופונים אלחוטיים", "Wireless Microphones Systems", 1833, 1847),
                raw("אוזניות", "Earphones", 1848, 1859),
                raw("ציוד הגברה היקפי", "Additional Stage Equipment", 1860, 1901)
        );

        buildGroupedItems();
    }

    private RawSub raw(String name, String folder, int minId, int maxId) {
        return new RawSub(name, folder, minId, maxId);
    }

    private void addCategory(String categoryKey, RawSub... raws) {
        List<Subcategory> list = new ArrayList<>();
        String categoryFolder = categoryFolders.get(categoryKey);
        for (RawSub r : raws) {
            Subcategory s = new Subcategory(nextId++, r.name, r.folder, r.minId, r.maxId);
            list.add(s);
            imageFolderById.put(s.getId(), categoryFolder + "/" + s.getFolder());
            subcategoryNameById.put(s.getId(), s.getName());
        }
        subcategoriesByCategory.put(categoryKey, list);
    }

    private void buildGroupedItems() {
        for (Map.Entry<String, List<Subcategory>> entry : subcategoriesByCategory.entrySet()) {
            String groupLabel = categoryNames.get(entry.getKey());
            SelectItemGroup group = new SelectItemGroup(groupLabel);
            SelectItem[] items = new SelectItem[entry.getValue().size()];
            int i = 0;
            for (Subcategory s : entry.getValue()) {
                items[i++] = new SelectItem(s.getId(), s.getName());
            }
            group.setSelectItems(items);
            groupedSubcategoryItems.add(group);
        }
    }

    // --- משמש את subcategories.xhtml - נשאר ללא שינוי ---
    public String getCategoryName(String categoryId) {
        return categoryNames.getOrDefault(categoryId, "");
    }

    // --- משמש את subcategories.xhtml - נשאר ללא שינוי ---
    public List<Subcategory> getSubcategories(String categoryId) {
        return subcategoriesByCategory.getOrDefault(categoryId, Collections.emptyList());
    }

    // --- חדש: לשימוש בתפריט הנפתח בעמוד הניהול ---
    public List<SelectItem> getGroupedSubcategoryItems() {
        return groupedSubcategoryItems;
    }

    // --- חדש: לתצוגה נוחה בטבלת הניהול ---
    public String getSubcategoryName(int categoryId) {
        return subcategoryNameById.getOrDefault(categoryId, "");
    }

    /**
     * מחזיר את הנתיב היחסי לתיקיית התמונות - לפי מזהה תת-הקטגוריה (categoryId),
     * לדוגמה "Keyboards/Synths". מחזיר מחרוזת ריקה אם לא נמצאה התאמה.
     *
     * שימו לב: זהו שינוי מהותי - הפרמטר עכשיו הוא categoryId ולא productId כמו קודם.
     */
    public String getImageFolder(int categoryId) {
        return imageFolderById.getOrDefault(categoryId, "");
    }
}