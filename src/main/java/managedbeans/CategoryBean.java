package managedbeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.model.SelectItem;
import jakarta.faces.model.SelectItemGroup;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.*;

/**
 * Application-wide static catalog of categories and subcategories.
 * Builds, once at startup, the category -> subcategory tree, the subcategory
 * ID -> image-folder mapping, and the grouped dropdown items used by the
 * admin "add/edit product" form.
 */
@Named("categoryBean")
@ApplicationScoped
public class CategoryBean implements Serializable {

    /* Represents a subcategory, including its unique ID, display name, image folder, and the product ID range associated with it */
    public static class Subcategory implements Serializable {
        private final int id;
        private final String name;
        private final String folder;
        private final int minId;
        private final int maxId;

        /* Creates a new subcategory definition */
        public Subcategory(int id, String name, String folder, int minId, int maxId) {
            this.id = id;
            this.name = name;
            this.folder = folder;
            this.minId = minId;
            this.maxId = maxId;
        }

        /* Returns the subcategory ID */
        public int getId() { return id; }

        /* Returns the display name of the subcategory */
        public String getName() { return name; }

        /* Returns the image folder associated with the subcategory */
        public String getFolder() { return folder; }

        /* Returns the first product ID in the subcategory range */
        public int getMinId() { return minId; }

        /* Returns the last product ID in the subcategory range */
        public int getMaxId() { return maxId; }
    }

    /* Temporary helper object used during initialization before stable subcategory IDs are assigned. */
    private static class RawSub {
        final String name, folder;
        final int minId, maxId;

        /* Creates a raw subcategory definition. */
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
    private final Map<Integer, String> imageFolderById = new LinkedHashMap<>();
    private final Map<Integer, String> subcategoryNameById = new LinkedHashMap<>();
    private final List<SelectItem> groupedSubcategoryItems = new ArrayList<>();

    private int nextId = 1;

    /* Initializes all category and subcategory definitions and builds the grouped dropdown items used throughout the application */
    @PostConstruct
    public void init() {

        /* Register all top-level categories and their Hebrew display names */
        categoryNames.put("keyboards", "קלידים");
        categoryNames.put("guitars", "גיטרות");
        categoryNames.put("drums", "תופים וכלי הקשה");
        categoryNames.put("wind", "כלי נשיפה");
        categoryNames.put("strings", "כלי קשת");
        categoryNames.put("studio", "ציוד לאולפן");
        categoryNames.put("stage", "ציוד במה");

        /* Map category keys to their corresponding image folder names */
        categoryFolders.put("keyboards", "Keyboards");
        categoryFolders.put("guitars", "Guitars");
        categoryFolders.put("drums", "Drums");
        categoryFolders.put("wind", "Wind Instruments");
        categoryFolders.put("strings", "String Instruments");
        categoryFolders.put("studio", "Studio Equipment");
        categoryFolders.put("stage", "Stage Equipment");

        /* Register all subcategories for each top-level category */
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

        /* Build grouped select items for use in dropdown menus */
        buildGroupedItems();
    }

    /* Creates a temporary raw subcategory definition */
    private RawSub raw(String name, String folder, int minId, int maxId) {
        /* Package the supplied data into a RawSub object */
        return new RawSub(name, folder, minId, maxId);
    }

    /* Assigns a stable incrementing ID to each raw subcategory and registers it in all the maps */
    private void addCategory(String categoryKey, RawSub... raws) {

        /* Create the subcategory collection for this category */
        List<Subcategory> list = new ArrayList<>();

        /* Retrieve the parent category's image folder */
        String categoryFolder = categoryFolders.get(categoryKey);

        /* Convert each raw definition into a fully registered subcategory */
        for (RawSub r : raws) {
            /* Create the subcategory and assign a unique incremental ID */
            Subcategory s = new Subcategory(nextId++, r.name, r.folder, r.minId, r.maxId);
            /* Add the subcategory to the category list */
            list.add(s);
            /* Store lookup information for image folders and display names */
            imageFolderById.put(s.getId(), categoryFolder + "/" + s.getFolder());
            subcategoryNameById.put(s.getId(), s.getName());
        }
        /* Register the completed subcategory list under its category */
        subcategoriesByCategory.put(categoryKey, list);
    }

    /* Builds the grouped (by category) dropdown items used in the admin form */
    private void buildGroupedItems() {
        /* Process each category and its subcategories */
        for (Map.Entry<String, List<Subcategory>> entry : subcategoriesByCategory.entrySet()) {
            /* Retrieve the category display name for the group label */
            String groupLabel = categoryNames.get(entry.getKey());
            /* Create a grouped dropdown section */
            SelectItemGroup group = new SelectItemGroup(groupLabel);
            /* Build SelectItem entries for each subcategory */
            SelectItem[] items = new SelectItem[entry.getValue().size()];
            int i = 0;
            for (Subcategory s : entry.getValue()) {
                /* Use the subcategory ID as the value and its name as the label */
                items[i++] = new SelectItem(s.getId(), s.getName());
            }
            /* Attach items to the group and register the group */
            group.setSelectItems(items);
            groupedSubcategoryItems.add(group);
        }
    }

    /* Returns the display name of a category by its key */
    public String getCategoryName(String categoryId) {
        /* Return the category name or an empty string if not found */
        return categoryNames.getOrDefault(categoryId, "");
    }

    /* Returns all subcategories belonging to the specified category */
    public List<Subcategory> getSubcategories(String categoryId) {
        /* Return the matching subcategory list or an empty list if absent */
        return subcategoriesByCategory.getOrDefault(categoryId, Collections.emptyList());
    }

    /* Returns the grouped subcategory items used by the admin dropdown menu */
    public List<SelectItem> getGroupedSubcategoryItems() {
        /* Return the prebuilt grouped dropdown structure */
        return groupedSubcategoryItems;
    }

    /* Returns the display name of a subcategory by its ID */
    public String getSubcategoryName(int categoryId) {
        /* Look up the subcategory name and return an empty string if missing */
        return subcategoryNameById.getOrDefault(categoryId, "");
    }

    /**
     * Returns the relative image-folder path for a given subcategory ID
     * (categoryId), e.g. "Keyboards/Synths". Returns an empty string if no
     * match is found.
     *
     * Note: this is a meaningful change from before - the parameter is now
     * categoryId, not productId as it used to be.
     */
    public String getImageFolder(int categoryId) {
        /* Retrieve the folder path for the specified subcategory */
        return imageFolderById.getOrDefault(categoryId, "");
    }
}
