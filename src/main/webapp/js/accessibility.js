(function () {
    "use strict";

    var STORAGE_KEY = "a11y-settings";
    var FONT_STEPS = ["a11y-font-m2", "a11y-font-m1", "", "a11y-font-1", "a11y-font-2", "a11y-font-3", "a11y-font-4"];
    var DEFAULT_FONT_INDEX = 2;
    var TOGGLE_CLASSES = ["a11y-contrast", "a11y-grayscale", "a11y-links", "a11y-readable-font", "a11y-stop-animations"];

    function loadSettings() {
        try {
            var raw = localStorage.getItem(STORAGE_KEY);
            if (!raw) return { fontIndex: DEFAULT_FONT_INDEX, toggles: {} };
            var parsed = JSON.parse(raw);
            if (typeof parsed.fontIndex !== "number") parsed.fontIndex = DEFAULT_FONT_INDEX;
            if (!parsed.toggles) parsed.toggles = {};
            return parsed;
        } catch (e) {
            return { fontIndex: DEFAULT_FONT_INDEX, toggles: {} };
        }
    }

    function saveSettings() {
        try {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(settings));
        } catch (e) { /* אחסון חסום - לא נורא */ }
    }

    var settings = loadSettings();

    function applyAll() {
        var html = document.documentElement;
        FONT_STEPS.forEach(function (cls) {
            if (cls) html.classList.remove(cls);
        });
        var fontClass = FONT_STEPS[settings.fontIndex];
        if (fontClass) html.classList.add(fontClass);

        TOGGLE_CLASSES.forEach(function (cls) {
            html.classList.toggle(cls, !!settings.toggles[cls]);
        });

        updateButtonStates();
    }

    function updateButtonStates() {
        TOGGLE_CLASSES.forEach(function (cls) {
            var btn = document.querySelector('.a11y-btn[data-toggle="' + cls + '"]');
            if (btn) btn.classList.toggle("a11y-active", !!settings.toggles[cls]);
        });
    }

    function changeFont(delta) {
        var next = settings.fontIndex + delta;
        if (next < 0) next = 0;
        if (next > FONT_STEPS.length - 1) next = FONT_STEPS.length - 1;
        settings.fontIndex = next;
        saveSettings();
        applyAll();
    }

    function toggleClass(cls) {
        settings.toggles[cls] = !settings.toggles[cls];
        saveSettings();
        applyAll();
    }

    function resetAll() {
        settings = { fontIndex: DEFAULT_FONT_INDEX, toggles: {} };
        saveSettings();
        applyAll();
    }

    function buildUI() {
        var btn = document.createElement("button");
        btn.id = "a11y-toggle-btn";
        btn.type = "button";
        btn.setAttribute("aria-label", "פתיחת תפריט נגישות");
        btn.innerHTML = '<span aria-hidden="true">&#9855;</span>';

        var panel = document.createElement("div");
        panel.id = "a11y-panel";
        panel.setAttribute("role", "dialog");
        panel.setAttribute("aria-label", "תפריט נגישות");
        panel.innerHTML =
            '<h2>נגישות</h2>' +
            '<div class="a11y-row">' +
            '<span>גודל טקסט</span>' +
            '<span>' +
            '<button type="button" class="a11y-btn" id="a11y-font-minus" aria-label="הקטן טקסט">A-</button> ' +
            '<button type="button" class="a11y-btn" id="a11y-font-plus" aria-label="הגדל טקסט">A+</button>' +
            '</span>' +
            '</div>' +
            '<div class="a11y-row"><button type="button" class="a11y-btn a11y-full-btn" data-toggle="a11y-contrast">ניגודיות גבוהה</button></div>' +
            '<div class="a11y-row"><button type="button" class="a11y-btn a11y-full-btn" data-toggle="a11y-grayscale">גווני אפור (שחור-לבן)</button></div>' +
            '<div class="a11y-row"><button type="button" class="a11y-btn a11y-full-btn" data-toggle="a11y-links">הדגשת קישורים</button></div>' +
            '<div class="a11y-row"><button type="button" class="a11y-btn a11y-full-btn" data-toggle="a11y-readable-font">פונט קריא</button></div>' +
            '<div class="a11y-row"><button type="button" class="a11y-btn a11y-full-btn" data-toggle="a11y-stop-animations">עצירת אנימציות</button></div>' +
            '<button type="button" id="a11y-reset-btn">איפוס הכל</button>';

        document.body.appendChild(btn);
        document.body.appendChild(panel);

        btn.addEventListener("click", function () {
            panel.classList.toggle("a11y-open");
        });

        document.getElementById("a11y-font-plus").addEventListener("click", function () { changeFont(1); });
        document.getElementById("a11y-font-minus").addEventListener("click", function () { changeFont(-1); });
        document.getElementById("a11y-reset-btn").addEventListener("click", resetAll);

        Array.prototype.forEach.call(panel.querySelectorAll(".a11y-btn[data-toggle]"), function (b) {
            b.addEventListener("click", function () {
                toggleClass(b.getAttribute("data-toggle"));
            });
        });

        document.addEventListener("click", function (e) {
            if (!panel.contains(e.target) && e.target !== btn && !btn.contains(e.target)) {
                panel.classList.remove("a11y-open");
            }
        });
    }

    applyAll();

    document.addEventListener("DOMContentLoaded", function () {
        buildUI();
        applyAll();
    });
})();