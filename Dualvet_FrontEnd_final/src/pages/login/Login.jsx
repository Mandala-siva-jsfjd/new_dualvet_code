import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../assets/css/Login.css';
import logo from '../../assets/images/logo1.png';
import { createTheme, ThemeProvider, CssBaseline, Container, Box, Typography, Grid, TextField, Button, IconButton, InputAdornment } from '@mui/material';
import { AuthContext } from '../../security/AuthContext';
import { toast } from 'react-toastify';
import Loading from '../../Loding/loding';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';

const defaultTheme = createTheme();

const Login = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [error, setError] = useState('');
  const [attemptCount, setAttemptCount] = useState(0);
  const [isLocked, setIsLocked] = useState(false); // Track account lock status
  const navigate = useNavigate();
  const { login, googleLogin, loading, setLoading } = useContext(AuthContext);
  const [showPassword, setShowPassword] = useState(false);

  const encryptPassword = async (password) => {
    const encoder = new TextEncoder();
    const data = encoder.encode(password);

    const key = new Uint8Array([0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
      0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
      0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
      0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F]);

    const cryptoKey = await crypto.subtle.importKey(
      'raw',
      key,
      { name: 'AES-GCM', length: 256 },
      false,
      ['encrypt']
    );

    const iv = crypto.getRandomValues(new Uint8Array(12));

    const encrypted = await crypto.subtle.encrypt(
      {
        name: 'AES-GCM',
        iv: iv,
      },
      cryptoKey,
      data
    );

    const encryptedPassword = btoa(String.fromCharCode(...new Uint8Array(encrypted)));
    const ivString = btoa(String.fromCharCode(...iv));

    return `${ivString}:${encryptedPassword}`;
  };

  const handleClick = async (e) => {
    e.preventDefault();

    if (isLocked) {
      return;
    }

    const { email, password } = formData;

    if (!email.trim() || !password.trim()) {
      setError("Please enter both email and password.");
      return;
    }

    setLoading(true);
    try {
      const encryptedPayload = await encryptPassword(password);
      await login({ email, password: encryptedPayload });
      setError('');
      setAttemptCount(0);
      navigate("/dashboard");
      toast.success('Login successful');
    } catch (error) {
      const newAttemptCount = attemptCount + 1;
      setAttemptCount(newAttemptCount);

      if (newAttemptCount >= 3) {
        setIsLocked(true);
        setError("Your account is locked due to multiple failed login attempts. Please contact the IT team to unlock your account.");
      } else {
        setError("Invalid email or password.");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleClickShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const handleGoogleLoginSuccess = async (credentialResponse) => {
    const { credential } = credentialResponse;
  
    setLoading(true);
    try {
      // Using the googleLogin method from context
      await googleLogin(credential);
      navigate("/dashboard");
      toast.success('Login successful');
    } catch (error) {
      setError('Google login failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };
  
  const clientId = process.env.REACT_APP_GOOGLE_CLIENT_ID;

  return (
    <div className='main-head'>
      <div className='main-head-logo'>
        <img className='navbar-logo' src={logo} alt="not valid" />
      </div>
      <div className='login-container'>
        <div className='home-head'>
          <b className="welcome-title">Welcome to </b><br />

          <b className="brand-name">TATA STRIVE</b>

          <p className="description" style={{ textAlign: "justify" }}>
            Tata STRIVE is a Skill Development programme housed under the overall governance of Tata Community Initiatives Trust (TCIT) which was established in October 2014 as a public charitable trust by Tata Sons Private Limited.
          </p>
        </div>
        <ThemeProvider theme={defaultTheme}>
          <div className='Login-main'>
            <Container component="main" maxWidth="xs">
              <CssBaseline />
              <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>

      
                <Typography component="h1" variant="h5"><b>Login</b></Typography>

                <Box component="form" noValidate sx={{ mt: 3 }}>
                  <Grid container spacing={2}>
                    <Grid item xs={12}>
                      <TextField
                        required
                        fullWidth
                        id="email"
                        label="Email"
                        name="email"
                        autoComplete="email"
                        value={formData.email}
                        onChange={handleInputChange}
                        disabled={isLocked}
                      />
                    </Grid>
                    <Grid item xs={12}>
                      <TextField
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type={showPassword ? "text" : "password"}
                        id="password"
                        autoComplete="new-password"
                        value={formData.password}
                        onChange={handleInputChange}
                        disabled={isLocked}
                        InputProps={{
                          endAdornment: (
                            <InputAdornment position="end">
                              <IconButton
                                aria-label="toggle password visibility"
                                onClick={handleClickShowPassword}
                                edge="end"
                                disabled={isLocked}
                              >
                                {showPassword ? <VisibilityOff /> : <Visibility />}
                              </IconButton>
                            </InputAdornment>
                          ),
                        }} />
                    </Grid>
                  </Grid>
                  {error && (
                    <Typography color="error" variant="body2" sx={{ mt: 1 }}>
                      {error}
                    </Typography>
                  )}
                  <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    sx={{ mt: 3, mb: 2 }}
                    onClick={handleClick}
                    disabled={isLocked}
                  >
                    Login
                  </Button>
                  <Typography className="or-text">- OR -</Typography>
                  <GoogleOAuthProvider clientId={clientId}>
                    <GoogleLogin
                      onSuccess={handleGoogleLoginSuccess}
                      onError={() => setError('Google login failed. Please try again.')}
                      text="continue_with"
                      shape="pill"
                    />
                  </GoogleOAuthProvider>
                </Box>
              </Box>
            </Container>
          </div>
        </ThemeProvider>
      </div>
      <Loading open={loading} />
    </div>
  );
};

export default Login;

