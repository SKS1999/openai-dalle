package com.webomax.openai.presentation.generate_image


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.muratozturk.click_shrink_effect.applyClickShrink
import com.webomax.openai.R
import com.webomax.openai.common.*
import com.webomax.openai.databinding.FragmentGenerateImageBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


@AndroidEntryPoint
class GenerateImageFragment : Fragment(R.layout.fragment_generate_image) {
    private final var TAG = "MainActivity"
    private var mRewardedAd: RewardedAd? = null

    private val viewModel: GenerateImageViewModel by viewModels()

    private val binding by viewBinding(FragmentGenerateImageBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewCollect()

        MobileAds.initialize(this@GenerateImageFragment.requireContext()) { initstatus ->
            Log.d(TAG, "onCreate:$initstatus")

        }
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("TEST_DEVICE_ID_HERE", "TEST_DEVICE_TO_HERE"))
                .build()
        )
        loadRewardAd()


    }

    private fun loadRewardAd() {
        var adRequest = AdRequest.Builder().build()
        RewardedAd.load(this@GenerateImageFragment.requireContext(),
            "ca-app-pub-4420523761768723/6091354377",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, "onAdFailedToLoad: ${adError.message}")
                    Toast.makeText(
                        this@GenerateImageFragment.requireContext(),
                        "you can only watch per day",
                        Toast.LENGTH_SHORT
                    ).show()
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    Log.d(TAG, "onAdLoaded: ")
                    mRewardedAd = rewardedAd

                }
            })


    }

    private fun show() {
        if (mRewardedAd != null) {

            mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(TAG, "Ad dismissed fullscreen content.")
                    mRewardedAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.")

                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.")
                }


            }
            mRewardedAd?.show(this@GenerateImageFragment.requireActivity()) {
                Log.d(TAG, "showRewardedAd")

                Toast.makeText(
                    this@GenerateImageFragment.requireContext(),
                    "Reward Earned..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this@GenerateImageFragment.requireContext(),
                "Ad wasn't loaded",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

    private fun loadAndShowAd() {
        val progressDialog = ProgressDialog(this@GenerateImageFragment.requireContext())
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Loading Reward Ad...!")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        var adRequest = AdRequest.Builder().build()
        RewardedAd.load(this@GenerateImageFragment.requireContext(),
            "ca-app-pub-4420523761768723/6091354377",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, "onAdFailedToLoad: ${adError.message}")
                    mRewardedAd = null
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@GenerateImageFragment.requireContext(),
                        "Failed to load the Ad ${adError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    Log.d(TAG, "onAdLoaded: ")
                    mRewardedAd = rewardedAd
                    progressDialog.dismiss()
                    show()

                }


            })

    }

    @SuppressLint("SuspiciousIndentation")
    private fun initViewCollect() {
        with(viewModel) {
            with(binding) {
                rewardAdBtn.setOnClickListener {
                    if (loadAndShowAd()==null) {
                        generateButton.isClickable = false
                        Toast.makeText(
                            this@GenerateImageFragment.requireContext(),
                            "Click on Watch Ad to use the app",
                            Toast.LENGTH_SHORT
                        ).show()

                        if (loadAndShowAd() !== null) {
                            generateButton.isClickable = true
                            Toast.makeText(
                                this@GenerateImageFragment.requireContext(),
                                "Now you can use the app",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else{
                        Toast.makeText(
                            this@GenerateImageFragment.requireContext(),
                            "You can use the app by once a day",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                    generateButton.setOnClickListener {
                        if (promptEditText.text.toString().isEmpty().not() && loadAndShowAd() !==null)
                         {

                            generateImage(promptEditText.text.toString(), 1, Sizes.SIZE_256)
                        } else {
                            promptInputLayout.error = getString(R.string.enter_prompt)


                        }





                        generatedImageCard.applyClickShrink()


                        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                            state.collect { response ->
                                when (response) {
                                    is Resource.Loading -> {
                                        generateButton.startAnimation()
                                        shimmerLayout.apply {
                                            startShimmer()
                                            visible()
                                        }
                                        generatedImagesGrid.gone()
                                    }
                                    is Resource.Success -> {
                                        shimmerLayout.apply {
                                            stopShimmer()
                                            gone()
                                        }
                                        generatedImagesGrid.visible()

                                        generateButton.revertAnimation {
                                            generateButton.setBackgroundResource(R.drawable.rounded_bg3)
                                        }

                                        generatedImageView.glideImage(response.data.data[0].url)



                                        generatedImageCard.setOnClickListener {
                                            showImageFullPage(response.data.data[0].url)
                                        }


                                    }
                                    is Resource.Error -> {
                                        shimmerLayout.apply {
                                            stopShimmer()
                                            gone()
                                        }
                                        generatedImagesGrid.gone()

                                        generateButton.revertAnimation {
                                            generateButton.setBackgroundResource(R.drawable.rounded_bg3)
                                        }

                                        MotionToast.createColorToast(
                                            requireActivity(),
                                            getString(R.string.error),
                                            response.throwable.localizedMessage ?: "Error",
                                            MotionToastStyle.ERROR,
                                            MotionToast.GRAVITY_TOP or MotionToast.GRAVITY_CENTER,
                                            MotionToast.LONG_DURATION,
                                            null
                                        )

                                        Log.e(
                                            "Response",
                                            response.throwable.localizedMessage ?: "Error"
                                        )
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }

                }
            }
        }


            private fun showImageFullPage(imageUrl: String) {
                findNavController().navigate(
                    GenerateImageFragmentDirections.actionGenerateImageFragmentToImageDetailFragment(
                        imageUrl
                    )
                )
            }


    }



