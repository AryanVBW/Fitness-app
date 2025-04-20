package com.example.fitnesstracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private LibraryAdapter adapter;
    private List<LibraryItem> libraryItems;
    private Map<String, List<LibraryContent>> libraryContent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        
        // Initialize data
        initializeLibraryData();
        
        // Set up RecyclerView
        recyclerView = findViewById(R.id.libraryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Create and set adapter
        adapter = new LibraryAdapter(libraryItems, this);
        recyclerView.setAdapter(adapter);
        
        // Setup bottom navigation
        setupBottomNavigation();
        
        // Keep the Movement badge
        Button movementBadge = findViewById(R.id.movementBadge);
        movementBadge.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
            startActivity(browserIntent);
        });
    }
    
    private void initializeLibraryData() {
        // Initialize lists
        libraryItems = new ArrayList<>();
        libraryContent = new HashMap<>();
        
        // Add library categories
        libraryItems.add(new LibraryItem("Workout Guides", "Learn proper form and techniques for effective workouts", R.drawable.ic_workout));
        libraryItems.add(new LibraryItem("Nutrition Information", "Understand macros, meal timing, and dietary best practices", R.drawable.ic_nutrition));
        libraryItems.add(new LibraryItem("Fitness Tips", "General advice for fitness success and motivation", R.drawable.ic_tips));
        libraryItems.add(new LibraryItem("Recovery & Stretching", "Importance of recovery and proper stretching techniques", R.drawable.ic_recovery));
        
        // Populate content for each category
        
        // Workout Guides content
        List<LibraryContent> workoutGuides = new ArrayList<>();
        workoutGuides.add(new LibraryContent("Proper Squat Form", "Learn how to perform squats with correct form to maximize results and prevent injury.", 
                "Squats are a fundamental compound exercise that targets multiple muscle groups, primarily the quadriceps, hamstrings, and glutes.\n\n" +
                "Key points for proper form:\n" +
                "• Stand with feet shoulder-width apart\n" +
                "• Keep your back straight and chest up\n" +
                "• Lower your body as if sitting in a chair\n" +
                "• Keep knees aligned with toes (don't let them collapse inward)\n" +
                "• Descend until thighs are parallel to the floor (or as low as comfortable)\n" +
                "• Push through your heels to return to starting position\n\n" +
                "Common mistakes to avoid:\n" +
                "• Rounding your back\n" +
                "• Letting knees cave inward\n" +
                "• Rising onto your toes\n" +
                "• Not going deep enough\n\n" +
                "Start with bodyweight squats before adding resistance. Focus on quality over quantity."));
                
        workoutGuides.add(new LibraryContent("Bench Press Technique", "Master the bench press for chest development with these form tips.", 
                "The bench press is one of the most effective exercises for developing upper body strength, particularly in the chest, shoulders, and triceps.\n\n" +
                "Proper bench press form:\n" +
                "• Lie flat on the bench with feet firmly on the floor\n" +
                "• Grip the bar slightly wider than shoulder-width\n" +
                "• Unrack the bar and position it over your chest\n" +
                "• Lower the bar in a controlled manner to mid-chest\n" +
                "• Press the bar upward until arms are fully extended\n" +
                "• Keep wrists straight and elbows at approximately 45-degree angles\n\n" +
                "Safety tips:\n" +
                "• Always use a spotter for heavy lifts\n" +
                "• Don't bounce the bar off your chest\n" +
                "• Maintain proper back arch (natural curve)\n" +
                "• Keep your shoulders retracted and down"));
                
        workoutGuides.add(new LibraryContent("Deadlift Fundamentals", "Learn the proper deadlift technique for building overall strength.", 
                "The deadlift is a powerful compound movement that engages almost every major muscle group in the body.\n\n" +
                "Deadlift setup and execution:\n" +
                "• Position feet hip-width apart under the barbell\n" +
                "• Bend at hips and knees, grasp the bar with hands shoulder-width apart\n" +
                "• Keep chest up, back flat, and shoulders pulled back\n" +
                "• Drive through your heels, extending hips and knees simultaneously\n" +
                "• Keep the bar close to your body throughout the movement\n" +
                "• Stand fully upright at the top, shoulders back\n" +
                "• Lower the weight by hinging at the hips first, then bending knees\n\n" +
                "Important considerations:\n" +
                "• Start with lighter weights to master form\n" +
                "• Never round your lower back\n" +
                "• Engage your core throughout the movement\n" +
                "• Breathe properly: inhale before lifting, exhale at the top"));
        
        // Add more workout guides as needed
        libraryContent.put("Workout Guides", workoutGuides);
        
        // Nutrition Information content
        List<LibraryContent> nutritionInfo = new ArrayList<>();
        nutritionInfo.add(new LibraryContent("Understanding Macronutrients", "Learn about proteins, carbs, and fats and their role in fitness.", 
                "Macronutrients are the nutrients your body needs in large amounts to function properly: protein, carbohydrates, and fats.\n\n" +
                "Protein:\n" +
                "• Function: Builds and repairs muscle tissue, produces enzymes and hormones\n" +
                "• Sources: Meat, fish, eggs, dairy, legumes, tofu\n" +
                "• Recommended intake: 1.6-2.2g per kg of bodyweight for active individuals\n\n" +
                "Carbohydrates:\n" +
                "• Function: Primary energy source, especially for high-intensity activities\n" +
                "• Sources: Grains, fruits, vegetables, legumes\n" +
                "• Types: Simple (quick energy) vs. Complex (sustained energy)\n" +
                "• Timing: Beneficial before and after workouts\n\n" +
                "Fats:\n" +
                "• Function: Hormone production, vitamin absorption, cell health\n" +
                "• Sources: Avocados, nuts, seeds, olive oil, fatty fish\n" +
                "• Types: Unsaturated (healthier) vs. Saturated (limit intake)\n\n" +
                "Balance is key: Your specific macro needs depend on your fitness goals, body type, and activity level."));
                
        nutritionInfo.add(new LibraryContent("Pre and Post Workout Nutrition", "Optimize your performance and recovery with proper meal timing.", 
                "Strategic nutrition timing around your workouts can significantly impact your performance and results.\n\n" +
                "Pre-Workout Nutrition (1-3 hours before):\n" +
                "• Purpose: Provide energy, prevent muscle breakdown\n" +
                "• Focus on: Moderate protein and carbohydrates, low fat\n" +
                "• Examples: Greek yogurt with fruit, oatmeal with protein powder, turkey sandwich\n" +
                "• Hydration: 16-20oz water\n\n" +
                "Immediate Pre-Workout (30-60 minutes before):\n" +
                "• Purpose: Quick energy source\n" +
                "• Focus on: Simple carbs, easily digestible\n" +
                "• Examples: Banana, sports drink, small serving of dried fruit\n\n" +
                "Post-Workout (within 45 minutes):\n" +
                "• Purpose: Muscle recovery, glycogen replenishment\n" +
                "• Focus on: Protein and carbohydrates\n" +
                "• Protein: 20-40g high-quality protein\n" +
                "• Carbs: 0.5-0.7g per kg of bodyweight\n" +
                "• Examples: Protein shake with banana, chicken and rice, chocolate milk\n\n" +
                "Hydration: Replace 16-24oz fluid for every pound lost during exercise"));
        
        nutritionInfo.add(new LibraryContent("Hydration Guidelines", "Learn how proper hydration affects your performance and recovery.", 
                "Proper hydration is critical for optimal performance, recovery, and overall health.\n\n" +
                "Daily Hydration Needs:\n" +
                "• Minimum recommendation: 0.5oz of water per pound of bodyweight daily\n" +
                "• Active individuals: Additional 16-24oz for every hour of exercise\n" +
                "• Signs of proper hydration: Clear to light yellow urine\n\n" +
                "Hydration Timeline:\n" +
                "• 2-3 hours before workout: 16-20oz water\n" +
                "• 15 minutes before: 8-10oz water\n" +
                "• During exercise: 7-10oz every 10-20 minutes\n" +
                "• After exercise: 16-24oz for every pound lost\n\n" +
                "Electrolytes:\n" +
                "• Important for: Muscle function, nerve signaling, pH balance\n" +
                "• Key electrolytes: Sodium, potassium, magnesium, calcium\n" +
                "• When to use electrolyte drinks: Workouts >60 minutes, intense exercise, hot environments\n\n" +
                "Hydration Tips:\n" +
                "• Carry a water bottle throughout the day\n" +
                "• Set reminders to drink regularly\n" +
                "• Consume water-rich foods (fruits, vegetables)\n" +
                "• Limit alcohol and caffeine, which can contribute to dehydration"));
        
        // Add more nutrition info as needed
        libraryContent.put("Nutrition Information", nutritionInfo);
        
        // Fitness Tips content
        List<LibraryContent> fitnessTips = new ArrayList<>();
        fitnessTips.add(new LibraryContent("Setting SMART Fitness Goals", "Learn how to set effective fitness goals that you can achieve.", 
                "SMART goal setting provides a framework for creating effective fitness objectives.\n\n" +
                "SMART stands for:\n" +
                "• Specific: Define exactly what you want to accomplish\n" +
                "• Measurable: Include concrete numbers to track progress\n" +
                "• Achievable: Be realistic about what you can accomplish\n" +
                "• Relevant: Ensure goals align with your interests and lifestyle\n" +
                "• Time-bound: Set a deadline to create urgency\n\n" +
                "Examples of SMART fitness goals:\n" +
                "• \"I will walk 10,000 steps daily for the next 30 days\"\n" +
                "• \"I will increase my squat weight by 20 pounds within 8 weeks\"\n" +
                "• \"I will reduce my body fat percentage from 25% to 20% in 3 months\"\n\n" +
                "Tips for success:\n" +
                "• Write down your goals\n" +
                "• Track progress consistently\n" +
                "• Break larger goals into smaller milestones\n" +
                "• Adjust goals as needed based on progress\n" +
                "• Celebrate achievements to maintain motivation"));
                
        fitnessTips.add(new LibraryContent("Overcoming Fitness Plateaus", "Strategies to break through when your progress stalls.", 
                "Plateaus are a normal part of any fitness journey. Here's how to overcome them.\n\n" +
                "Signs you've hit a plateau:\n" +
                "• No progress in strength, endurance, or body composition for 2-3 weeks\n" +
                "• Decreased motivation or workout enjoyment\n" +
                "• Feeling stuck despite consistent effort\n\n" +
                "Strategies to break through plateaus:\n\n" +
                "1. Change your workout variables:\n" +
                "• Increase intensity (weight, speed, incline)\n" +
                "• Modify volume (sets, reps, duration)\n" +
                "• Alter rest periods between sets\n" +
                "• Try new exercises that target the same muscle groups\n\n" +
                "2. Implement periodization:\n" +
                "• Cycle between different training phases\n" +
                "• Include deload weeks to allow full recovery\n\n" +
                "3. Reassess nutrition:\n" +
                "• Adjust calorie intake if needed\n" +
                "• Review macronutrient balance\n" +
                "• Consider meal timing strategies\n\n" +
                "4. Prioritize recovery:\n" +
                "• Ensure adequate sleep (7-9 hours)\n" +
                "• Manage stress levels\n" +
                "• Consider active recovery activities\n\n" +
                "Remember: Plateaus are opportunities to reassess and improve your approach."));
                
        fitnessTips.add(new LibraryContent("Staying Motivated", "Tips and tricks to maintain your fitness motivation long-term.", 
                "Maintaining motivation is crucial for long-term fitness success.\n\n" +
                "Intrinsic motivation strategies:\n" +
                "• Identify your \"why\" - the deeper reason behind your fitness journey\n" +
                "• Focus on how exercise makes you feel, not just how it makes you look\n" +
                "• Choose activities you genuinely enjoy\n" +
                "• Celebrate non-scale victories (energy levels, mood improvements, etc.)\n\n" +
                "External motivation boosters:\n" +
                "• Find a workout buddy or community\n" +
                "• Work with a personal trainer or coach\n" +
                "• Sign up for events or challenges\n" +
                "• Use fitness tracking apps to visualize progress\n" +
                "• Reward yourself for reaching milestones\n\n" +
                "Habit formation techniques:\n" +
                "• Schedule workouts like important appointments\n" +
                "• Stack fitness habits with existing routines\n" +
                "• Start with small, achievable changes\n" +
                "• Create environmental triggers (like laying out workout clothes)\n" +
                "• Use the \"don't break the chain\" method for consistency\n\n" +
                "When motivation wanes:\n" +
                "• Return to your fundamental \"why\"\n" +
                "• Adjust your goals if needed\n" +
                "• Try something completely new\n" +
                "• Remember that discipline trumps motivation"));
        
        // Add more fitness tips as needed
        libraryContent.put("Fitness Tips", fitnessTips);
        
        // Recovery content
        List<LibraryContent> recoveryContent = new ArrayList<>();
        recoveryContent.add(new LibraryContent("Importance of Rest Days", "Why rest days are crucial for your fitness progress.", 
                "Rest days are not just for physical recovery—they're essential for progress and preventing injury.\n\n" +
                "Why rest days matter:\n" +
                "• Muscle repair and growth occurs during rest, not during workouts\n" +
                "• Prevents overtraining syndrome and associated symptoms\n" +
                "• Allows nervous system recovery\n" +
                "• Replenishes energy stores (glycogen)\n" +
                "• Reduces risk of injury and burnout\n\n" +
                "Signs you need a rest day:\n" +
                "• Persistent fatigue or decreased performance\n" +
                "• Increased resting heart rate\n" +
                "• Excessive soreness that doesn't improve\n" +
                "• Poor sleep quality\n" +
                "• Irritability or decreased motivation\n" +
                "• Frequent illness or injuries\n\n" +
                "Types of rest:\n" +
                "• Complete rest: No structured exercise\n" +
                "• Active recovery: Light activity like walking, swimming, or yoga\n\n" +
                "Optimal rest frequency:\n" +
                "• Beginners: Take a rest day between workout days\n" +
                "• Intermediate: 2-3 rest days per week\n" +
                "• Advanced: At least 1-2 rest days per week, with additional active recovery days\n\n" +
                "Remember: Rest is not laziness—it's an essential component of any effective fitness program."));
                
        recoveryContent.add(new LibraryContent("Effective Stretching Techniques", "Learn proper stretching methods to improve flexibility and prevent injury.", 
                "Proper stretching improves flexibility, enhances performance, and reduces injury risk.\n\n" +
                "Types of stretching:\n\n" +
                "1. Static stretching:\n" +
                "• Hold a stretch position for 15-60 seconds\n" +
                "• Best performed after workouts or as a separate flexibility session\n" +
                "• Avoid before high-intensity activities\n" +
                "• Examples: Seated hamstring stretch, standing quad stretch\n\n" +
                "2. Dynamic stretching:\n" +
                "• Controlled movements through full range of motion\n" +
                "• Ideal for pre-workout warm-up\n" +
                "• Prepares muscles and joints for activity\n" +
                "• Examples: Leg swings, arm circles, walking lunges\n\n" +
                "3. PNF (Proprioceptive Neuromuscular Facilitation):\n" +
                "• Advanced technique alternating contraction and relaxation\n" +
                "• Often requires a partner\n" +
                "• Very effective for increasing range of motion\n" +
                "• Example: Contract-relax hamstring stretch\n\n" +
                "Key stretching guidelines:\n" +
                "• Never stretch cold muscles\n" +
                "• Stretch to the point of tension, not pain\n" +
                "• Breathe deeply and relax into stretches\n" +
                "• Be consistent—daily stretching yields best results\n" +
                "• Focus on problem areas and activity-specific needs\n\n" +
                "Recommended stretching routine:\n" +
                "• Dynamic stretches before workout (5-10 minutes)\n" +
                "• Static stretches after workout (10-15 minutes)\n" +
                "• Hold each stretch 15-30 seconds\n" +
                "• Repeat each stretch 2-4 times"));
        
        // Add more recovery content as needed
        libraryContent.put("Recovery & Stretching", recoveryContent);
    }
    
    private void setupBottomNavigation() {
        LinearLayout workoutNavButton = findViewById(R.id.workoutNavButton);
        
        // Library button is already active
        workoutNavButton.setBackgroundResource(android.R.color.darker_gray);
        
        workoutNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    
    // Model classes for library items
    public static class LibraryItem {
        private String title;
        private String description;
        private int iconResource;
        
        public LibraryItem(String title, String description, int iconResource) {
            this.title = title;
            this.description = description;
            this.iconResource = iconResource;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getIconResource() {
            return iconResource;
        }
    }
    
    public static class LibraryContent {
        private String title;
        private String summary;
        private String content;
        
        public LibraryContent(String title, String summary, String content) {
            this.title = title;
            this.summary = summary;
            this.content = content;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getSummary() {
            return summary;
        }
        
        public String getContent() {
            return content;
        }
    }
    
    // Adapter for the RecyclerView
    public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
        
        private List<LibraryItem> items;
        private android.content.Context context;
        
        public LibraryAdapter(List<LibraryItem> items, android.content.Context context) {
            this.items = items;
            this.context = context;
        }
        
        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_library_category, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            LibraryItem item = items.get(position);
            holder.titleTextView.setText(item.getTitle());
            holder.descriptionTextView.setText(item.getDescription());
            
            // Set icon if available
            try {
                holder.iconView.setImageResource(item.getIconResource());
            } catch (Exception e) {
                // If resource not found, hide the image view
                holder.iconView.setVisibility(View.GONE);
            }
            
            // Set click listener
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCategoryContent(item.getTitle());
                }
            });
        }
        
        @Override
        public int getItemCount() {
            return items.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView descriptionTextView;
            CardView cardView;
            ImageView iconView;
            
            ViewHolder(View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.categoryTitle);
                descriptionTextView = itemView.findViewById(R.id.categoryDescription);
                cardView = itemView.findViewById(R.id.categoryCard);
                iconView = itemView.findViewById(R.id.categoryIcon);
            }
        }
    }
    
    // Show content for a specific category
    private void showCategoryContent(String category) {
        List<LibraryContent> contentList = libraryContent.get(category);
        
        if (contentList != null && !contentList.isEmpty()) {
            // Create dialog with list of content items
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(category);
            
            // Create list of content titles
            String[] titles = new String[contentList.size()];
            for (int i = 0; i < contentList.size(); i++) {
                titles[i] = contentList.get(i).getTitle();
            }
            
            builder.setItems(titles, (dialog, which) -> {
                // Show the selected content
                showContentDetails(contentList.get(which));
            });
            
            builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
            builder.show();
        } else {
            Toast.makeText(this, "No content available for this category", Toast.LENGTH_SHORT).show();
        }
    }
    
    // Show detailed content
    private void showContentDetails(LibraryContent content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(content.getTitle());
        
        // Create a ScrollView for long content
        android.widget.ScrollView scrollView = new android.widget.ScrollView(this);
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(30, 30, 30, 30);
        
        TextView contentText = new TextView(this);
        contentText.setText(content.getContent());
        contentText.setTextSize(16);
        contentText.setLineSpacing(0, 1.2f);
        
        layout.addView(contentText);
        scrollView.addView(layout);
        
        builder.setView(scrollView);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
