package com.example.upaymimoni.presentation.ui.profile.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.presentation.ui.auth.utils.createTempImageUri
import com.example.upaymimoni.presentation.ui.profile.components.CircularProfileImage
import com.example.upaymimoni.presentation.ui.common.components.EditProfileField
import com.example.upaymimoni.presentation.ui.profile.viewmodel.EditProfileViewModel
import com.example.upaymimoni.presentation.ui.profile.viewmodel.ErrorState
import com.example.upaymimoni.presentation.ui.profile.viewmodel.SaveChangesEvents
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    editViewModel: EditProfileViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val user by editViewModel.currentUser.collectAsState()

    val name by editViewModel.newName.collectAsState()
    val email by editViewModel.newEmail.collectAsState()
    val phone by editViewModel.newPhone.collectAsState()
    val error by editViewModel.errorState.collectAsState()

    val pendingImageUri by editViewModel.pendingImageUri.collectAsState()

    val saveEvent = editViewModel.saveEvent

    val snackBarHostState = remember { SnackbarHostState() }

    val isLoading by editViewModel.loading.collectAsState()
    val uploadProgress by editViewModel.uploadProgress.collectAsState()

    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    var showPicker by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { editViewModel.onImageSelected(it) }
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            tempImageUri?.let { editViewModel.onImageSelected(it) }
        }

        tempImageUri = null
    }

    val cameraPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createTempImageUri(context)
            tempImageUri = uri
            cameraLauncher.launch(uri)
        }
    }


    user?.let {
        LaunchedEffect(user) {
            editViewModel.initializeUser(it)

        }

        LaunchedEffect(Unit) {
            saveEvent.collect { event ->
                when (event) {
                    is SaveChangesEvents.NavigateToProfile -> {
                        onBackClick()
                    }

                    is SaveChangesEvents.ShowSnackbar -> {
                        snackBarHostState.showSnackbar(event.message)
                    }
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Edit Profile",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        titleContentColor = MaterialTheme.colorScheme.onTertiary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                )
            }
        ) { paddingValues ->
            EditProfileContent(
                currentUser = it,
                onUploadClick = { showPicker = true },
                name = name,
                updateName = editViewModel::updateName,
                email = email,
                updateEmail = editViewModel::updateEmail,
                phone = phone,
                updatePhone = editViewModel::updatePhone,
                onSaveClick = editViewModel::onSaveClick,
                errorState = error,
                pendingUri = pendingImageUri,
                isLoading = isLoading,
                modifier = Modifier.padding(paddingValues)
            )

            if (showPicker) {
                PickerPopup(
                    onDismissRequest = { showPicker = false },
                    onChooseGallery = {
                        showPicker = false
                        imagePickerLauncher.launch("image/*")
                    },
                    onChooseCamera = {
                        showPicker = false
                        cameraPermissionsLauncher.launch(Manifest.permission.CAMERA)
                    }
                )
            }

            if (isLoading) {
                LoadingOverlay(
                    progress = uploadProgress
                )
            }
        }
    }
}

@Composable
fun EditProfileContent(
    currentUser: User,
    onUploadClick: () -> Unit,
    name: TextFieldValue,
    updateName: (TextFieldValue) -> Unit,
    email: TextFieldValue,
    updateEmail: (TextFieldValue) -> Unit,
    phone: TextFieldValue,
    updatePhone: (TextFieldValue) -> Unit,
    onSaveClick: () -> Unit,
    errorState: ErrorState,
    pendingUri: Uri?,
    isLoading: Boolean,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = 48.dp, horizontal = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProfileImage(
            imageUrl = currentUser.profilePictureUrl,
            tempUri = pendingUri,
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .aspectRatio(1f),
            showUploadButton = true,
            onUploadClick = onUploadClick,
            isUploadEnabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            EditProfileField(
                value = name,
                isEnabled = !isLoading,
                label = "Name",
                placeHolder = "Enter your name",
                isError = errorState.nameError,
                onValueChange = { updateName(it) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Name Icon"
                    )
                },
                onFocusLost = {
                    updateName(name.copy(text = name.text.trim()))
                }
            )

            EditProfileField(
                value = email,
                isEnabled = !isLoading,
                label = "Email",
                placeHolder = "Enter your email",
                isError = errorState.emailError,
                onValueChange = { updateEmail(it) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon"
                    )
                }
            )

            EditProfileField(
                value = phone,
                isEnabled = !isLoading,
                label = "Mobile",
                placeHolder = "Enter your phone number",
                isError = errorState.numberError,
                onValueChange = { updatePhone(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon"
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = errorState.errorMsg ?: " ",
            color = if (errorState.nameError || errorState.emailError || errorState.numberError) {
                MaterialTheme.colorScheme.error
            } else {
                Color.Transparent
            },
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 16.dp, top = 4.dp, end = 16.dp)
                .defaultMinSize(minHeight = 24.dp)
                .wrapContentHeight(align = Alignment.Top)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))

        ElevatedButton(
            onClick = onSaveClick,
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Save Changes"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerPopup(
    onDismissRequest: () -> Unit,
    onChooseGallery: () -> Unit,
    onChooseCamera: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Select Image Source",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                OptionCard(
                    label = "Camera",
                    icon = Icons.Default.CameraAlt,
                    modifier = Modifier
                        .weight(1f),
                    onClick = onChooseCamera
                )

                OptionCard(
                    label = "Gallery",
                    icon = Icons.Default.Image,
                    modifier = Modifier
                        .weight(1f),
                    onClick = onChooseGallery
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun OptionCard(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "$label icon",
                modifier = Modifier
                    .size(32.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LoadingOverlay(progress: Double?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {}
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        val currentProgress = progress?.toFloat()

        if (currentProgress != null) {
            CircularProgressIndicator(
                progress = { currentProgress },
                modifier = Modifier
                    .size(196.dp),
                strokeWidth = 12.dp,
                color = MaterialTheme.colorScheme.tertiary
            )

            Text(
                text = "${(currentProgress * 100).toInt()}%",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(196.dp),
                strokeWidth = 12.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionCardPreview() {
    UPayMiMoniTheme {
        OptionCard(
            label = "Camera",
            icon = Icons.Default.CameraAlt,
            onClick = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun EditProfilePreview() {
    UPayMiMoniTheme {
        EditProfileContent(
            currentUser = User(
                id = "abcdef123456",
                profilePictureUrl = "https://api.dicebear.com/9.x/bottts-neutral/svg?seed=testUser",
                displayName = "Not a bot",
                phoneNumber = "88888888",
                email = "notabot@botters.com",
                groups = emptyList()
            ),
            onUploadClick = {},
            name = TextFieldValue(""),
            updateName = {},
            email = TextFieldValue(""),
            updateEmail = {},
            phone = TextFieldValue(""),
            updatePhone = {},
            onSaveClick = {},
            errorState = ErrorState(
                emailError = true,
                errorMsg = "Invalid Email"
            ),
            pendingUri = null,
            isLoading = false,
            modifier = Modifier.padding()
        )
    }
}