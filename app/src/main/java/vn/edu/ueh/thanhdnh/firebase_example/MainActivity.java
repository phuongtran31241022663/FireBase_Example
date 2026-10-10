package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private FirebaseAuth auth;
  FirebaseFirestore db;
  Button btAdd, btShow;
  EditText etTitle, etContent, etImageUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    FirebaseApp.initializeApp(this);
    auth = FirebaseAuth.getInstance();

    String email = "pt8803974@gmail.com";
    String password = "TyP02112006";

    // Only authenticate if not already logged in
    if (auth.getCurrentUser() == null) {
      auth.createUserWithEmailAndPassword(email, password)
          .addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
              Toast.makeText(MainActivity.this, "Registered & Logged in successfully!", Toast.LENGTH_SHORT).show();
            } else {
              auth.signInWithEmailAndPassword(email, password)
                  .addOnCompleteListener(signInTask -> {
                    if (signInTask.isSuccessful()) {
                      Toast.makeText(MainActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                      String errorMsg = signInTask.getException() != null ? signInTask.getException().getMessage() : "Unknown error";
                      Toast.makeText(MainActivity.this, "Authentication failed: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                  });
            }
          });
    }

    db = FirebaseFirestore.getInstance();
    btAdd = findViewById(R.id.btAdd);
    btShow = findViewById(R.id.btShow);
    etTitle = findViewById(R.id.etTitle);
    etContent = findViewById(R.id.etContent);
    etImageUrl = findViewById(R.id.etImageUrl);

    btAdd.setOnClickListener(this);
    btShow.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.btAdd) {
      String title = etTitle.getText().toString().trim();
      String content = etContent.getText().toString().trim();
      String imageUrl = etImageUrl.getText().toString().trim();

      if (!title.isEmpty() && !content.isEmpty()) {
        Article article = new Article(title, content, imageUrl);
        db.collection("articles").add(article)
            .addOnSuccessListener(documentReference -> {
              documentReference.update("id", documentReference.getId());
              Toast.makeText(this, "Article added to Firestore!", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> 
                Toast.makeText(this, "Error adding article: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        
        etTitle.setText("");
        etContent.setText("");
        etImageUrl.setText("");
      } else {
        Toast.makeText(this, "Please enter title and content", Toast.LENGTH_SHORT).show();
      }
    } else if (view.getId() == R.id.btShow) {
      Intent intent = new Intent(getBaseContext(), ShowDataActivity.class);
      startActivity(intent);
    }
  }
}
