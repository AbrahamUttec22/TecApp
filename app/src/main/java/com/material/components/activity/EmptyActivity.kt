package com.material.components.activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.alejandrolora.finalapp.goToActivity
import com.google.firebase.auth.FirebaseAuth
import com.material.components.R
import com.material.components.activity.login.LoginCardOverlap
import com.material.components.drawer.DashboarActivity
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import com.material.components.admin.AdminDashboardActivity
import kotlinx.android.synthetic.main.activity_splash.*
/**
 * @author Abraham
 */
class MainEmptyActivity : AppCompatActivity() {

    //instance for firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val myanim = AnimationUtils.loadAnimation(this, R.anim.floating_up)
        imageLogo.startAnimation(myanim)
        Handler().postDelayed({
            //mAuth.signOut()
            //Validated for know if the user us already login
            if (mAuth.currentUser == null) {
                goToActivity<LoginCardOverlap> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            } else {
                var correo=mAuth.currentUser!!.email.toString()
                if(correo.equals("guerrerog@gmail.com")){
                    goToActivity<AdminDashboardActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }else{
                    goToActivity<DashboarActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }

            }
        }, 4100)

    }//end for onCreate()
}