package com.devmoss.kabare.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart

object EmailSender {

//    // Configure email and password for SMTP authentication
    private const val SMTP_EMAIL = "devmossteam@gmail.com"  // Use your email
    private const val SMTP_PASSWORD = "auar utsu zgpw triy"  // Use your app-specific password
    // Function to send OTP email
    suspend fun sendOtpEmail(recipientEmail: String, otp: String, callback: (Boolean, String?) -> Unit) {
        withContext(Dispatchers.IO) {
            val properties = Properties().apply {
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")
                put("mail.smtp.host", "smtp.gmail.com")  // Gmail's SMTP server
                put("mail.smtp.port", "587")  // TLS port
            }

            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(SMTP_EMAIL, SMTP_PASSWORD)
                }
            })

            try {
                        // Premium HTML email content
                        val htmlContent = """
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f9f9f9;
                        color: #333;
                    }
                    .email-container {
                        max-width: 600px;
                        margin: 40px auto;
                        background-color: #ffffff;
                        border-radius: 8px;
                        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                        overflow: hidden;
                    }
                    .header {
                        background-color: #4A99FF; /* Warna biru baru */
                        color: #ffffff;
                        text-align: center;
                        padding: 20px;
                        font-size: 24px;
                        font-weight: bold;
                    }
                    .content {
                        padding: 20px;
                        text-align: center;
                    }
                    .otp-code {
                        display: inline-block;
                        margin: 20px 0;
                        padding: 10px 20px;
                        font-size: 32px;
                        font-weight: bold;
                        color: #4A99FF; /* Warna biru baru */
                        background-color: #f4f4f4;
                        border-radius: 4px;
                        letter-spacing: 2px;
                    }
                    .footer {
                        text-align: center;
                        padding: 10px;
                        background-color: #f4f4f4;
                        font-size: 12px;
                        color: #888;
                    }
                    .footer a {
                        color: #4A99FF; /* Warna biru baru */
                        text-decoration: none;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="header">
                        Your OTP Code
                    </div>
                    <div class="content">
                        <p>Hello,</p>
                        <p>Your One-Time Password (OTP) for verification is:</p>
                        <div class="otp-code">$otp</div>
                        <p>Please use this code within the next 5 minutes. If you did not request this, please ignore this email.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 DevMoss Team. All rights reserved.</p>
                        <p>This is an automated message. <a href="mailto:devmossteam@gmail.com">Contact us</a> if you need assistance.</p>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(SMTP_EMAIL))
                    addRecipient(Message.RecipientType.TO, InternetAddress(recipientEmail))
                    subject = "Your Secure OTP Code"

                    val bodyPart = MimeBodyPart()
                    bodyPart.setContent(htmlContent, "text/html")

                    val multipart = MimeMultipart()
                    multipart.addBodyPart(bodyPart)

                    setContent(multipart)
                }

                Transport.send(message)
                callback(true, "Email sent successfully!")
            } catch (e: Exception) {
                e.printStackTrace()
                callback(false, "Error sending email: ${e.message}")
            }
        }
    }
}