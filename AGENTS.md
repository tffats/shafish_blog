# AGENTS.md

This file contains guidelines and commands for agentic coding agents working in this repository.

## Repository Overview

This is a MkDocs Material static blog site with Chinese content. The site uses Docker for deployment and GitHub Actions for CI/CD.

## Build/Lint/Test Commands

### Local Development
```bash
# Serve locally (requires Docker)
docker-compose up

# Alternative: Direct Docker run
docker run --rm -it -p 8000:8000 -v ./:/docs shafish/mkdocs-material:9.6.20

# Build site (inside container)
mkdocs build

# Serve with live reload (inside container)
mkdocs serve
```

### Deployment
```bash
# Deploy to GitHub Pages
mkdocs gh-deploy --force

# Build for production
mkdocs build --clean
```

### Dependencies
```bash
# Install Python dependencies
pip install mkdocs-material mkdocs-git-revision-date-localized-plugin mkdocs-rss-plugin

# Check MkDocs version
pip show mkdocs-material
```

## Code Style Guidelines

### Markdown Files
- Use UTF-8 encoding
- Line endings: LF (Unix style)
- Max line length: 120 characters for readability
- Use 2 spaces for list indentation
- Front matter must be at the top of files

### Front Matter (YAML)
```yaml
---
title: "Page Title"
tags:
  - Tag1
  - Tag2
hide:
  - navigation
  - toc
date: 2024-01-01
draft: false
---
```

### Content Structure
- Use Chinese for content (site language: zh)
- Main headings should be `#` (H1)
- Subheadings follow hierarchical order (`##`, `###`, etc.)
- Include code examples with proper language tags
- Use relative paths for internal links

### Code Blocks
```markdown
```python
def example():
    return "Hello World"
```

```yaml
# YAML example
key: value
```
```

### Images
- Use relative paths when possible
- Include alt text: `![Description](path/to/image.png)`
- For external images, use CDN URLs: `https://picture.cdn.shafish.cn/...`
- Add zoom class for clickable images: `![Description](path){: .zoom}`

### Links
- Internal: `[Text](path/to/page.md)`
- External: `[Text](https://example.com){target=_blank}`
- Email: `[Text](mailto:email@example.com)`

### Admonitions
```markdown
!!! note "Optional Title"
    Content here

!!! tip
    Important information
```

### Tables
```markdown
| Header 1 | Header 2 |
|----------|----------|
| Cell 1   | Cell 2   |
```

## File Organization

### Directory Structure
```
docs/
├── index.md              # Homepage
├── blog/                 # Blog posts
├── newBlog/              # New blog system
│   ├── posts/           # Blog posts
│   └── index.md         # Blog index
├── english/             # English content
├── life/                # Personal content
├── kafka/               # Kafka documentation
├── Java_Guide/          # Java tutorials
└── about.md             # About page
```

### Naming Conventions
- Files: kebab-case (e.g., `docker-note.md`)
- Directories: kebab-case or snake_case
- Chinese filenames are acceptable for content
- Use descriptive names

## MkDocs Configuration

### Required Plugins
- `search`: Built-in search
- `git-revision-date-localized`: Show last modified dates
- `rss`: RSS feed generation
- `blog`: Blog functionality
- `tags`: Tag management

### Theme Settings
- Material for MkDocs
- Language: Chinese (zh)
- Color scheme: Slate (dark)
- Primary color: Deep orange
- Navigation features enabled

## Content Guidelines

### Blog Posts
- Must include front matter with title, date, tags
- Use `<!-- more -->` for excerpt separation
- Categories for organization
- Draft mode for WIP content

### Technical Documentation
- Include code examples
- Use proper syntax highlighting
- Add explanations for complex concepts
- Include configuration examples

### Language
- Primary: Chinese (Simplified)
- Technical terms: Keep original English when appropriate
- Code comments: Use English for international compatibility

## Git Workflow

### Branch Strategy
- `main`: Primary branch
- `github-page`: GitHub Pages deployment branch

### Commit Messages
- Use Chinese for commit messages (repository convention)
- Format: `type: description`
- Types: feat, fix, docs, style, refactor, test

### Deployment
- Automatic deployment via GitHub Actions
- Manual deployment: `mkdocs gh-deploy --force`

## Testing

### Link Validation
```bash
# Check for broken links (if available)
mkdocs build --strict
```

### Content Review
- Verify all internal links work
- Check image rendering
- Test code block syntax highlighting
- Validate YAML front matter

## Security Considerations

- No sensitive data in configuration files
- Use environment variables for secrets
- Review external dependencies regularly
- Keep MkDocs and plugins updated

## Performance Optimization

- Optimize images before adding
- Use lazy loading for large images
- Minimize external dependencies
- Enable caching in production

## Common Issues

### Docker Issues
- Ensure volume mounting is correct
- Check port conflicts
- Verify image exists locally or pulls successfully

### Build Issues
- Check YAML syntax in mkdocs.yml
- Verify plugin compatibility
- Ensure all required dependencies are installed

### Content Issues
- Validate Markdown syntax
- Check for proper front matter
- Verify image paths and URLs

用中文回复