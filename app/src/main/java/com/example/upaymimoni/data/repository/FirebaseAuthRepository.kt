package com.example.upaymimoni.data.repository

import com.example.upaymimoni.data.mappers.ErrorMapper
import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.model.UpdateUserError
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.model.UserUpdateResult
import com.example.upaymimoni.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val updateErrorMapper: ErrorMapper<Exception, UpdateUserError>,
    private val authErrorMapper: ErrorMapper<Exception, AuthError>
) : AuthRepository {

    override suspend fun loginUser(email: String, password: String): AuthResult<User> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("Login Auth returned null user")

        AuthResult.Success(createNewUser(firebaseUser))

    } catch (e: Exception) {
        val domainError = authErrorMapper.map(e)
        AuthResult.Failure(domainError)
    }

    override suspend fun registerUser(email: String, password: String): AuthResult<User> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("Register Auth returned null user")

        AuthResult.Success(createNewUser(firebaseUser))

    } catch (e: Exception) {
        val domainError = authErrorMapper.map(e)
        AuthResult.Failure(domainError)
    }

    override suspend fun loginUserWithGoogle(idToken: String): AuthResult<User> = try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        val firebaseUser =
            result.user ?: throw Exception("Login With Google Auth returned null user")

        AuthResult.Success(createNewUser(firebaseUser))
    } catch (e: Exception) {
        val domainError = authErrorMapper.map(e)
        AuthResult.Failure(domainError)
    }

    override suspend fun sendResetPasswordEmail(email: String): AuthResult<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        // Just return a Unit if we are successful, this saves us from having to create a custom class.
        AuthResult.Success(
            Unit
        )
    } catch (e: Exception) {
        val domainError = authErrorMapper.map(e)
        AuthResult.Failure(domainError)
    }

    override suspend fun logoutUser() {
        auth.signOut()
    }

    override suspend fun updateUserProfile(
        newEmail: String,
        newDisplayName: String
    ): UserUpdateResult {
        val user = auth.currentUser
            ?: return UserUpdateResult.Failure(UpdateUserError.Unknown("No authenticated user found, this should never happen"))

        return try {
            if (user.email != newEmail) {
                user.updateEmail(newEmail).await()
            }

            if (user.displayName != newDisplayName) {
                val profileUpdate = userProfileChangeRequest {
                    displayName = newDisplayName
                }
                user.updateProfile(profileUpdate).await()
            }

            val updatedUser = auth.currentUser
                ?: return UserUpdateResult.Failure(UpdateUserError.Unknown("User was not found after update"))

            UserUpdateResult.Success(createNewUser(updatedUser))

        } catch (e: Exception) {
            val domainError = updateErrorMapper.map(e)
            UserUpdateResult.Failure(domainError)
        }
    }

    private fun createNewUser(firebaseUser: FirebaseUser): User {
        return User(
            id = firebaseUser.uid,
            profilePictureUrl = firebaseUser.photoUrl?.toString(),
            displayName = firebaseUser.displayName,
            phoneNumber = firebaseUser.phoneNumber,
            email = firebaseUser.email,
            groups = emptyList()
        )
    }
}