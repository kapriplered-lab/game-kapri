package id.levelup.reallife;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final int BG = Color.rgb(15, 22, 18);
    private static final int PANEL = Color.rgb(27, 39, 31);
    private static final int PANEL2 = Color.rgb(35, 49, 39);
    private static final int GOLD = Color.rgb(224, 190, 112);
    private static final int GREEN = Color.rgb(116, 191, 130);
    private static final int TEXT = Color.rgb(239, 239, 222);
    private static final int MUTED = Color.rgb(166, 177, 163);

    private SharedPreferences prefs;
    private LinearLayout page;
    private String tab = "hero";
    private int level = 1, xp = 0, coins = 0, skillPoints = 0;
    private JSONArray quests = new JSONArray();
    private JSONArray skills = new JSONArray();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("levelup_save", MODE_PRIVATE);
        loadState();
        render();
    }

    private void loadState() {
        level = prefs.getInt("level", 1);
        xp = prefs.getInt("xp", 0);
        coins = prefs.getInt("coins", 0);
        skillPoints = prefs.getInt("skillPoints", 0);
        try {
            String savedQuests = prefs.getString("quests", null);
            if (savedQuests != null) quests = new JSONArray(savedQuests);
            else seedQuests();
            String savedSkills = prefs.getString("skills", null);
            if (savedSkills != null) skills = new JSONArray(savedSkills);
            else seedSkills();
        } catch (JSONException e) {
            quests = new JSONArray(); skills = new JSONArray();
            seedQuests(); seedSkills();
        }
        saveState();
    }

    private void seedQuests() {
        addQuestData("Gerakkan badan 10 menit", "VITALITY", 20, 10, false);
        addQuestData("Belajar hal baru 20 menit", "INTELLIGENCE", 25, 12, false);
        addQuestData("Rapikan satu area kecil", "DISCIPLINE", 15, 8, false);
        addQuestData("Selesaikan satu tugas penting", "DISCIPLINE", 30, 15, false);
    }

    private void seedSkills() {
        skills = new JSONArray();
        addSkillData("Warrior's Grit", "WARRIOR", "Konsistensi fisik dan keberanian memulai", 2, false);
        addSkillData("Scholar's Focus", "SCHOLAR", "Belajar dengan fokus dan rasa ingin tahu", 2, false);
        addSkillData("Guardian's Oath", "GUARDIAN", "Menjaga rutinitas dan tanggung jawab", 2, false);
    }

    private void saveState() {
        prefs.edit().putInt("level", level).putInt("xp", xp).putInt("coins", coins)
                .putInt("skillPoints", skillPoints).putString("quests", quests.toString())
                .putString("skills", skills.toString()).apply();
    }

    private String today() { return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()); }

    private void addQuestData(String title, String stat, int rewardXp, int rewardCoins, boolean custom) {
        JSONObject q = new JSONObject();
        try { q.put("id", System.currentTimeMillis() + quests.length()); q.put("title", title); q.put("stat", stat);
            q.put("xp", rewardXp); q.put("coins", rewardCoins); q.put("custom", custom); q.put("completedDate", "");
            quests.put(q); } catch (JSONException ignored) { }
    }

    private void addSkillData(String name, String branch, String desc, int requiredLevel, boolean unlocked) {
        JSONObject s = new JSONObject();
        try { s.put("name", name); s.put("branch", branch); s.put("desc", desc); s.put("requiredLevel", requiredLevel); s.put("unlocked", unlocked); skills.put(s); }
        catch (JSONException ignored) { }
    }

    private void render() {
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(BG);
        root.setPadding(dp(16), dp(12), dp(16), dp(8));
        LinearLayout brand = new LinearLayout(this); brand.setOrientation(LinearLayout.VERTICAL);
        TextView title = text("✦  LEVEL UP: REAL LIFE", 19, GOLD, true);
        TextView subtitle = text("YOUR REAL LIFE · YOUR ADVENTURE", 10, MUTED, true);
        brand.addView(title); brand.addView(subtitle); root.addView(brand, matchWrap());
        LinearLayout nav = new LinearLayout(this); nav.setOrientation(LinearLayout.HORIZONTAL); nav.setPadding(0, dp(12), 0, dp(10));
        nav.addView(navButton("Hero", "hero"), new LinearLayout.LayoutParams(0, dp(42), 1));
        nav.addView(navButton("Quests", "quests"), new LinearLayout.LayoutParams(0, dp(42), 1));
        nav.addView(navButton("Skill Tree", "skills"), new LinearLayout.LayoutParams(0, dp(42), 1));
        root.addView(nav);
        ScrollView scroll = new ScrollView(this); scroll.setFillViewport(true);
        page = new LinearLayout(this); page.setOrientation(LinearLayout.VERTICAL); page.setPadding(0, dp(2), 0, dp(16));
        scroll.addView(page); root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
        if ("quests".equals(tab)) renderQuests(); else if ("skills".equals(tab)) renderSkills(); else renderHero();
    }

    private Button navButton(String label, final String target) {
        Button b = button(label, target.equals(tab) ? GOLD : PANEL2, target.equals(tab) ? BG : TEXT);
        b.setTextSize(12); b.setOnClickListener(v -> { tab = target; render(); }); return b;
    }

    private void renderHero() {
        addSpace(page, 8);
        LinearLayout hero = card(); hero.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView emblem = text("♜", 44, GOLD, true); emblem.setGravity(Gravity.CENTER); hero.addView(emblem);
        hero.addView(text("THE WAYFARER", 12, MUTED, true));
        hero.addView(text("Level " + level + " Adventurer", 24, TEXT, true));
        hero.addView(text(xp + " / " + xpNeeded() + " XP TO NEXT LEVEL", 11, GOLD, true));
        ProgressBar progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setMax(xpNeeded()); progress.setProgress(Math.min(xp, xpNeeded())); progress.setProgressTintList(android.content.res.ColorStateList.valueOf(GOLD));
        progress.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(PANEL2));
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, dp(8)); pp.setMargins(0, dp(10), 0, dp(8)); hero.addView(progress, pp);
        hero.addView(text("◈  " + coins + " GOLD     ✦  " + skillPoints + " SKILL POINTS", 13, GOLD, true));
        page.addView(hero, matchWrap());

        TextView heading = text("CHARACTER ATTRIBUTES", 14, GOLD, true); heading.setPadding(2, dp(18), 0, dp(8)); page.addView(heading);
        LinearLayout stats = new LinearLayout(this); stats.setOrientation(LinearLayout.HORIZONTAL);
        stats.addView(statCard("⚔", "STRENGTH", "Build momentum"), new LinearLayout.LayoutParams(0, dp(100), 1));
        stats.addView(statCard("✧", "INTELLECT", "Learn daily"), new LinearLayout.LayoutParams(0, dp(100), 1));
        stats.addView(statCard("⬟", "VITALITY", "Move your body"), new LinearLayout.LayoutParams(0, dp(100), 1));
        page.addView(stats);
        TextView daily = text("TODAY'S ADVENTURE", 14, GOLD, true); daily.setPadding(2, dp(18), 0, dp(8)); page.addView(daily);
        int total = quests.length(), done = 0;
        for (int i = 0; i < quests.length(); i++) if (isDoneToday(quests.optJSONObject(i))) done++;
        LinearLayout summary = card(); summary.addView(text(done + " of " + total + " quests completed", 16, TEXT, true));
        summary.addView(text(done == total ? "All clear, adventurer. Rest well." : "Every small action adds to your legend.", 12, MUTED, false));
        Button go = button("OPEN QUEST BOARD  →", PANEL2, GOLD); go.setOnClickListener(v -> { tab = "quests"; render(); }); summary.addView(go); page.addView(summary);
    }

    private View statCard(String icon, String name, String hint) {
        LinearLayout c = card(); c.setPadding(dp(9), dp(12), dp(5), dp(8)); c.addView(text(icon, 22, GOLD, true)); c.addView(text(name, 10, TEXT, true)); c.addView(text(hint, 9, MUTED, false));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, dp(100), 1); p.setMargins(0, 0, dp(5), 0); c.setLayoutParams(p); return c;
    }

    private void renderQuests() {
        page.addView(text("QUEST BOARD", 22, GOLD, true));
        page.addView(text("Complete a quest once per day to earn its rewards.", 12, MUTED, false));
        Button add = button("＋  CREATE CUSTOM QUEST", PANEL2, GOLD); add.setOnClickListener(v -> showAddQuest());
        LinearLayout.LayoutParams ap = matchWrap(); ap.setMargins(0, dp(12), 0, dp(8)); page.addView(add, ap);
        for (int i = 0; i < quests.length(); i++) {
            final int index = i; JSONObject q = quests.optJSONObject(i); if (q == null) continue;
            LinearLayout c = card(); c.setPadding(dp(14), dp(12), dp(14), dp(12));
            c.addView(text(q.optString("title", "Untitled Quest"), 16, TEXT, true));
            c.addView(text(q.optString("stat", "DISCIPLINE") + "  ·  +" + q.optInt("xp", 15) + " XP  ·  +" + q.optInt("coins", 5) + " GOLD", 11, GOLD, true));
            if (isDoneToday(q)) {
                TextView done = text("✓ COMPLETED TODAY", 12, GREEN, true); done.setPadding(0, dp(8), 0, 0); c.addView(done);
            } else {
                Button claim = button("COMPLETE QUEST  +XP", GREEN, BG); claim.setOnClickListener(v -> completeQuest(index));
                LinearLayout.LayoutParams cp = matchWrap(); cp.setMargins(0, dp(9), 0, 0); c.addView(claim, cp);
            }
            if (q.optBoolean("custom", false)) {
                Button remove = button("Remove custom quest", PANEL2, MUTED); remove.setOnClickListener(v -> { quests.remove(index); saveState(); render(); }); c.addView(remove);
            }
            LinearLayout.LayoutParams lp = matchWrap(); lp.setMargins(0, dp(8), 0, 0); page.addView(c, lp);
        }
    }

    private void renderSkills() {
        page.addView(text("SKILL TREE", 22, GOLD, true));
        page.addView(text("Spend skill points to unlock your personal growth paths.", 12, MUTED, false));
        LinearLayout wallet = card(); wallet.addView(text("Available skill points: " + skillPoints, 17, GOLD, true)); page.addView(wallet);
        for (int i = 0; i < skills.length(); i++) {
            final int index = i; JSONObject s = skills.optJSONObject(i); if (s == null) continue;
            LinearLayout c = card(); c.addView(text(branchIcon(s.optString("branch")) + "  " + s.optString("branch") + " PATH", 10, GOLD, true));
            c.addView(text(s.optString("name"), 17, TEXT, true)); c.addView(text(s.optString("desc"), 12, MUTED, false));
            if (s.optBoolean("unlocked", false)) c.addView(text("✦ UNLOCKED", 12, GREEN, true));
            else {
                String requirement = "Requires level " + s.optInt("requiredLevel", 2);
                Button unlock = button(level < s.optInt("requiredLevel", 2) ? requirement : "UNLOCK · 1 SKILL POINT", PANEL2, level >= s.optInt("requiredLevel", 2) ? GOLD : MUTED);
                unlock.setEnabled(level >= s.optInt("requiredLevel", 2) && skillPoints > 0);
                unlock.setOnClickListener(v -> unlockSkill(index)); c.addView(unlock);
            }
            LinearLayout.LayoutParams lp = matchWrap(); lp.setMargins(0, dp(10), 0, 0); page.addView(c, lp);
        }
        TextView note = text("Skills are currently milestones and identity paths. Deeper gameplay bonuses can be added in a later version.", 11, MUTED, false); note.setPadding(4, dp(14), 4, 0); page.addView(note);
    }

    private String branchIcon(String branch) { if ("WARRIOR".equals(branch)) return "⚔"; if ("SCHOLAR".equals(branch)) return "✧"; return "⬟"; }
    private boolean isDoneToday(JSONObject q) { return q != null && today().equals(q.optString("completedDate", "")); }

    private void completeQuest(int index) {
        JSONObject q = quests.optJSONObject(index); if (q == null || isDoneToday(q)) return;
        try { q.put("completedDate", today()); } catch (JSONException ignored) { }
        xp += q.optInt("xp", 15); coins += q.optInt("coins", 5);
        while (xp >= xpNeeded()) { xp -= xpNeeded(); level++; skillPoints++; }
        saveState(); render();
    }

    private int xpNeeded() { return 100 + (level - 1) * 25; }

    private void unlockSkill(int index) {
        JSONObject s = skills.optJSONObject(index); if (s == null || s.optBoolean("unlocked", false)) return;
        if (level < s.optInt("requiredLevel", 2) || skillPoints < 1) return;
        try { s.put("unlocked", true); } catch (JSONException ignored) { }
        skillPoints--; saveState(); render();
    }

    private void showAddQuest() {
        LinearLayout form = new LinearLayout(this); form.setOrientation(LinearLayout.VERTICAL); form.setPadding(dp(20), dp(8), dp(20), 0);
        EditText name = new EditText(this); name.setSingleLine(true); name.setHint("Quest name"); name.setTextColor(TEXT); name.setHintTextColor(MUTED); form.addView(name);
        new AlertDialog.Builder(this).setTitle("Create a quest").setView(form).setNegativeButton("Cancel", null).setPositiveButton("Add", (dialog, which) -> {
            String value = name.getText().toString().trim(); if (value.isEmpty()) return;
            addQuestData(value, "DISCIPLINE", 20, 10, true); saveState(); render();
        }).show();
    }

    private LinearLayout card() {
        LinearLayout c = new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setPadding(dp(16), dp(14), dp(16), dp(14));
        GradientDrawable bg = new GradientDrawable(); bg.setColor(PANEL); bg.setCornerRadius(dp(16)); bg.setStroke(dp(1), Color.rgb(55, 72, 57)); c.setBackground(bg); return c;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color); t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD); t.setPadding(0, dp(2), 0, dp(2)); return t;
    }

    private Button button(String label, int background, int foreground) {
        Button b = new Button(this); b.setText(label); b.setTextColor(foreground); b.setTextSize(12); b.setAllCaps(false); b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        GradientDrawable shape = new GradientDrawable(); shape.setColor(background); shape.setCornerRadius(dp(12)); b.setBackground(shape); b.setPadding(dp(10), dp(4), dp(10), dp(4)); return b;
    }

    private void addSpace(LinearLayout target, int height) { View v = new View(this); target.addView(v, new LinearLayout.LayoutParams(1, dp(height))); }
    private LinearLayout.LayoutParams matchWrap() { return new LinearLayout.LayoutParams(-1, -2); }
    private int dp(float value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }
}
