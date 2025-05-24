package com.kshirsa.notificationservice;

public class EmailTemplates {

    public static String otpHtmlTemplate = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta http-equiv="Content-Type" content="text/html; charset=us-ascii">
                        <style>
                            body {
                                font-family: Arial, Helvetica, sans-serif;
                                line-height: 1.6;
                                background-color: #f4f4f4;
                                margin: 0;
                                padding: 20px;
                            }
                            .container {
                                max-width: 600px;
                                margin: auto;
                                background: #fff;
                                padding: 20px;
                                border-radius: 5px;
                                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                                text-align: center;
                                font-size: 16px;
                            }
                            .otp-text {
                                background-color: #018665;
                                color: white;
                                padding: 10px 40px;
                                border-radius: 4px;
                                display: inline-block;
                                margin: 20px 0;
                                font-size: 20px;
                                font-weight: bold;
                            }
                            .footer {
                                font-size: 14px;
                                color: #777;
                                margin-top: 20px;
                            }
                        </style>
                    </head>
                    <body>
                    <div class="container">
                        <h2 style="color: #018665;">Welcome to Kshirsa!</h2>
                        <p>%s</p>
                        <p style="font-size: 18px; font-weight: bold;">VERIFICATION CODE</p>
                        <div class="otp-text">%s</div>
                        <p>This OTP is valid for only <b>%s</b> minutes.</p>
                        <p>If you didn't sign up for an account with us, you can safely ignore this email.</p>
                        <p>Warm Regards,<br><span style="color: #018665;"><b>Kshirsa Team</b></span></p>
                        <div class="footer">© 2025 Kshirsa. All rights reserved.</div>
                    </div>
                    </body>
                    </html>
            
            """;

    public static String welcomeHtml = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <style>
                    body {
                        font-family: Arial, Helvetica, sans-serif;
                        line-height: 1.6;
                        background-color: #f4f4f4;
                        margin: 0;
                        padding: 20px;
                    }
                    .container {
                        max-width: 600px;
                        margin: auto;
                        background: #fff;
                        padding: 20px;
                        border-radius: 5px;
                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                        text-align: center;
                    }
                    .header {
                        color: #018665;
                        font-size: 26px;
                        font-weight: bold;
                        margin-bottom: 10px;
                    }
                    .message {
                        font-size: 16px;
                        color: #333;
                        margin: 20px 0;
                    }
                    .cta-button {
                        display: inline-block;
                        background-color: #018665;
                        color: white;
                        padding: 12px 25px;
                        border-radius: 4px;
                        text-decoration: none;
                        font-weight: bold;
                        font-size: 16px;
                        margin-top: 20px;
                    }
                    .footer {
                        font-size: 14px;
                        color: #777;
                        margin-top: 20px;
                    }
                </style>
            </head>
            <body>
            <div class="container">
                <h2 class="header">Welcome to Kshirsa!</h2>
                <p class="message"><strong>Hi there!</strong></p>
                <p class="message">We're thrilled to have you on board! Kshirsa is your one-stop personal finance app.</p>
                <p class="message"><strong>Quick Tip to Get Started:</strong><br>Complete your profile today to unlock personalized tips and take control of your finances like a pro!</p>
                <p class="footer">Cheers,<br><span style="color: #018665;"><b>Team Kshirsa</b></span></p>
                <div class="footer">© 2025 Kshirsa. All rights reserved.</div>
            </div>
            </body>
            </html>
            """;

    public static String newDeviceLogin = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <style>
                    body {
                        font-family: Arial, Helvetica, sans-serif;
                        line-height: 1.6;
                        background-color: #f4f4f4;
                        margin: 0;
                        padding: 20px;
                    }
                    .container {
                        max-width: 600px;
                        margin: auto;
                        background: #fff;
                        padding: 20px;
                        border-radius: 5px;
                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                        text-align: center;
                    }
                    .header {
                        color: #018665;
                        font-size: 24px;
                        font-weight: bold;
                    }
                    .message {
                        font-size: 16px;
                        color: #333;
                        margin: 20px 0;
                    }
                    .cta-button {
                        display: inline-block;
                        background-color: #018665;
                        color: white;
                        padding: 12px 25px;
                        border-radius: 4px;
                        text-decoration: none;
                        font-weight: bold;
                        font-size: 16px;
                        margin-top: 20px;
                    }
                    .footer {
                        font-size: 14px;
                        color: #777;
                        margin-top: 20px;
                    }
                </style>
            </head>
            <body>
            <div class="container">
                <h2 class="header">Login Alert for Kshirsa</h2>
                <p class="message">Hey,</p>
                <p class="message">We noticed a login to your Kshirsa account from a new device:</p>
                <p class="message"><b>Details:</b></p>
                <ul style="list-style: none; padding: 0; text-align: left; color: #333;">
                    <li><b>IP Address:</b> %s</li>
                    <li><b>Location:</b> %s</li>
                    <li><b>Time:</b> %s </li>
                </ul>
                <p class="message">If this was you, you can safely ignore this email. If you did not initiate this login, please secure your account immediately.</p>
                <p class="footer">Warm Regards,<br><span style="color: #018665;"><b>Team Kshirsa</b></span></p>
                <div class="footer">© 2025 Kshirsa. All rights reserved.</div>
            </div>
            </body>
            </html>
            """;

}
