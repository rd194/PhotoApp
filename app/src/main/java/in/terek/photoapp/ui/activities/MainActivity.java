package in.terek.photoapp.ui.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.terek.photoapp.BaseApplication;
import in.terek.photoapp.R;
import in.terek.photoapp.db.ImageFiles;
import in.terek.photoapp.zgallery.ZGallery;
import in.terek.photoapp.zgallery.entities.ZColor;

/**
 * @author Rajdeep yadav
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;


    /**
     * Views binding
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mRootView;

    /**
     * Constants
     */
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CAMERA = 1001;


    private Context mContext;
    private File imagePath;
    private View headerView;
    private boolean sentToSettings;
    private SharedPreferences permissionStatus;
    private String mCurrentPhotoPath;
    private Uri uri;
    private ImageFiles dbImageFile;

    @NonNull
    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, MainActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        headerView = navView.getHeaderView(0);
        mContext = this;
        permissionStatus = getSharedPreferences("photoApp", MODE_PRIVATE);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(AuthActivity.createIntent(this));
            finish();
            return;
        }

        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mRootView, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mRootView.addDrawerListener(toggle);
        toggle.syncState();


        navView.setNavigationItemSelectedListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraOpen();
            }
        });
        populateProfile(response);
        //populateIdpToken(response);


        ArrayList<String> imageUrlList = getImageUrlList();

        ZGallery.with(this, imageUrlList)
                .setToolbarTitleColor(ZColor.WHITE) // toolbar title color
                .setGalleryBackgroundColor(ZColor.WHITE) // activity background color
                .setToolbarColorResId(R.color.colorPrimary) // toolbar color
                .setTitle("Zak Gallery") // toolbar title
                .show();


    }

    private ArrayList<String> getImageUrlList() {
        List<ImageFiles> imageFiles = BaseApplication.daoSession.getImageFilesDao().loadAll();

        ArrayList<String> list=new ArrayList<>();

        for (ImageFiles imageFile : imageFiles) {
            list.add(imageFile.getServerUriPath());
        }
        return list;
    }

  /*  private void openCamera() {

    }*/


    public void openCamera() {
       Pix.start(this, REQUEST_CAMERA, 1);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_signout) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

                    Uri imageUri = getImageUri(returnValue.get(0));

                    if(imageUri!=null) {

                        CropImage.activity(imageUri)
                                .start(this);
                    }
                    else{
                        Toast.makeText(this,"Some error occurred.",Toast.LENGTH_SHORT).show();
                    }

             /*       Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    uri = getImageUri(MainActivity.this, bitmap);
                  //  saveBitmapToCache(bitmap);
                  //  imageViewUser.setImageBitmap(retrieveBitmapFromCache());


                 *//*   Uri data1 = data.getData();
                    uploadImage(retrieveBitmapFromCache());*//*
                    uploadImage(uri);*/
                } else {
                    Toast.makeText(mContext, "Some error occured", Toast.LENGTH_LONG).show();
                }
                break;

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        uploadImage(resultUri);
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }

                break;
            default:

        }
    }

    private Uri getImageUri(String s) {
        try {
            URL url = new URL("file://" + s);
            URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
            return Uri.parse(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, null, null);
        return Uri.parse(path);
    }

/*    public void saveBitmapToCache(Bitmap bitmap) {
        Cache.getInstance().getLru().put("user_image", bitmap);
    }


    public Bitmap retrieveBitmapFromCache() {
        Bitmap bitmap = (Bitmap) Cache.getInstance().getLru().get("user_image");
        AppSharedPreference.putString(Consts.SP_KEY_USER_IMAGE, ImageConversion.encodeToBase64(bitmap), ProfileActivity.this);
        return bitmap;
    }*/

    private void uploadImage(Uri uri) {
        dbImageFile=new ImageFiles();
        dbImageFile.setCreationDate(new Date());
       // dbImageFile.setFileName(uri.getLastPathSegment());

        dbImageFile.setLocalUriPath(uri.toString());
        FirebaseStorage instance = FirebaseStorage.getInstance();
        StorageReference reference = instance.getReference();
        StorageReference reference1 = FirebaseStorage.getInstance().getReference("images"+ UUID.randomUUID().toString());
        // reference.child("images/" + UUID.randomUUID().toString()+".jpeg");
        reference.child("images").child(uri.getLastPathSegment());
        reference1.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "onSuccess: ");
                        dbImageFile.setServerUriPath(taskSnapshot.getDownloadUrl().toString());
                        BaseApplication.daoSession.getImageFilesDao().insert(dbImageFile);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });

    }


    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(AuthActivity.createIntent(MainActivity.this));
                            finish();
                        } else {
                            Log.w(TAG, "signOut:failure", task.getException());
                            showSnackbar(R.string.sign_out_failed);
                        }
                    }
                });
    }


    private void populateProfile(@Nullable IdpResponse response) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        TextView nav_user = headerView.findViewById(R.id.tvName);
        TextView tvEmailMobile = headerView.findViewById(R.id.tvEmailMobile);
        ImageView imgUserImage = headerView.findViewById(R.id.imgUserImage);
        nav_user.setText(user.getDisplayName() != null ? user.getDisplayName() : "");

        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(imgUserImage);
        }

        tvEmailMobile.setText(TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
    }

    public void requestCameraOpen() {


        if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Camera and Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            openCamera();
        }


    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                requestCameraOpen();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[2])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

}
