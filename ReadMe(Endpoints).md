# Forum API – Endpoint Reference

## Authentication (`/api/auth`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register user |
| POST | /api/auth/login | Login and get JWT |

## User Profile (`/api/user`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/user/me | Get own profile |
| PUT | /api/user/me | Update own profile |
| GET | /api/user/{id} | Get public user profile |

## Avatar (`/api/user/me/avatar`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/user/me/avatar | Upload avatar |
| DELETE | /api/user/me/avatar | Delete avatar |

## Posts (`/api/posts`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/posts | List posts |
| GET | /api/posts/{id} | Get post |
| POST | /api/posts | Create post |
| PUT | /api/posts/{id} | Edit post |
| DELETE | /api/posts/{id} | Delete post |
| GET | /api/posts/category/{slug} | Posts by category |

## Comments (`/api`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/posts/{postId}/comments | Create comment |
| GET | /api/posts/{postId}/comments | List comments |
| PUT | /api/comments/{id} | Edit comment |
| DELETE | /api/comments/{id} | Delete comment |

## Likes (`/api`)
### Post Likes
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/posts/{id}/like | Like post |
| DELETE | /api/posts/{id}/like | Unlike post |
| GET | /api/posts/{id}/likes | Count post likes |

### Comment Likes
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/comments/{id}/like | Like comment |
| DELETE | /api/comments/{id}/like | Unlike comment |
| GET | /api/comments/{id}/likes | Count comment likes |

## Categories (`/api/categories`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/categories | List categories |
| GET | /api/categories/{slug} | Get category |

### Admin Category Management (`/api/admin/categories`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/admin/categories | Create category |
| PATCH | /api/admin/categories/{id} | Update category |
| DELETE | /api/admin/categories/{id} | Delete category |

## Search (`/api/search`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/search/posts?query= | Search posts |
| GET | /api/search/users?query= | Search users |

## Admin Panel (`/api/admin`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/admin/posts/{id}/pin | Pin post |
| POST | /api/admin/posts/{id}/lock | Lock post |
| DELETE | /api/admin/users/{id} | Ban user |
| PUT | /api/admin/users/{id}/role | Change user role |

## Moderator Panel (`/api/mod`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/mod/posts/{id}/pin | Pin post |
| POST | /api/mod/posts/{id}/lock | Lock post |
| DELETE | /api/mod/posts/{id} | Delete post |
| DELETE | /api/mod/comments/{id} | Delete comment |

